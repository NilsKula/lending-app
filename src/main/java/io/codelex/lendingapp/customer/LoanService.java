package io.codelex.lendingapp.customer;

import io.codelex.lendingapp.TimeSupplier;
import io.codelex.lendingapp.customer.api.Extension;
import io.codelex.lendingapp.customer.api.Loan;
import io.codelex.lendingapp.customer.api.LoanRequest;
import io.codelex.lendingapp.customer.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final ClientRepository clientRepository;
    private final ExtensionRepository extensionRepository;
    private final TimeSupplier timeSupplier;
    private final InterestService interestService;

    List<Loan> loans() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findByEmail(principal.toString().toLowerCase().trim()).orElse(null);
        return loanRepository.findAllByClient(client)
                .stream()
                .map(this::loan)
                .collect(Collectors.toList());
    }

    private Loan loan(LoanRecord loanRecord) {
        List<Extension> extensions = extensionRepository.findAllByLoanRecord(loanRecord);

        return new Loan(
                loanRecord.getLoanId(),
                loanRecord.getLoanStatus(),
                loanRecord.getCreated().toLocalDate(),
                loanRecord.getDueDate(),
                loanRecord.getPrincipal(),
                loanRecord.getInterest(),
                loanRecord.getTotal(),
                extensions
        );
    }


    void applyForLoan(HttpServletRequest request, LoanRequest loanRequest) {

        final String userIp = request.getHeader("X-FORWARDED-FOR");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Client client = clientRepository.findByEmail(principal.toString().toLowerCase().trim()).orElse(null);
        if (loanRepository.isLoanPresent(client)) {
            throw new InvalidRequestException();
        }

        LocalDateTime created = timeSupplier.getTime();
        LocalDate dueDate = LocalDate.from(created.plusDays(loanRequest.getDays()));

        BigDecimal interest = interestService.calculate(loanRequest.getAmount(), loanRequest.getDays());
        BigDecimal total = loanRequest.getAmount().add(interest);

        LoanRecord loanRecord = new LoanRecord();
        loanRecord.setLoanStatus("OPEN");
        loanRecord.setCreated(created);
        loanRecord.setDueDate(dueDate);
        loanRecord.setPrincipal(loanRequest.getAmount());
        loanRecord.setInterest(interest);
        loanRecord.setTotal(total);
        loanRecord.setClient(client);
        loanRecord.setInitialRequestDays(loanRequest.getDays());
        loanRecord.setIp(userIp);

        loanRepository.save(loanRecord);

    }

    Loan extend(String id, Integer extendRequest) {
        Object userEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (extendRequest == null) {
            throw new InvalidStatusException();
        }
        if (extendRequest < 7 || extendRequest > 30) {
            throw new InvalidStatusException();
        }

        LocalDate created = LocalDate.from(timeSupplier.getTime());
        LoanRecord loan = loanRepository.findByLoanId(id);
        if (loan == null) {
            throw new InvalidStatusException();
        }

        if (!loan.getClient().equals(clientRepository.findByEmail(userEmail.toString().toLowerCase().trim()).orElseThrow(null))) {
            throw new InvalidStatusException();
        }

        LocalDate dueDate = loan.getDueDate();

        BigDecimal principal = loan.getPrincipal();
        BigDecimal interest = interestService.calculate(loan.getPrincipal(), loan.getInitialRequestDays());
        BigDecimal total = loan.getPrincipal().add(interest);
        BigDecimal extensionInterest = interestService.calculateExtension(total, extendRequest);
        BigDecimal updatedTotal = principal.add(interest).add(extensionInterest);

        loan.setDueDate(dueDate.plusDays(extendRequest));
        loan.setInterest(interest.add(extensionInterest));
        loan.setTotal(updatedTotal);
        loan = loanRepository.save(loan);
        extensionRepository.save(new ExtensionRecord(loan, created, extendRequest, extensionInterest));

        return loan(loan);
    }
}
