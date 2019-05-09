package io.codelex.lendingapp.customer;


import io.codelex.lendingapp.TimeSupplier;
import io.codelex.lendingapp.customer.api.Loan;
import io.codelex.lendingapp.customer.api.LoanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static io.codelex.lendingapp.customer.ApplicationResponse.Status.APPROVED;
import static io.codelex.lendingapp.customer.ApplicationResponse.Status.REJECTED;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final RiskService riskService;
    private final TimeSupplier timeSupplier;

    @GetMapping("/loans")
    public List<Loan> loans() {
        return loanService.loans();
    }

    @PostMapping("/loans/apply")
    public ResponseEntity<ApplicationResponse> apply(HttpServletRequest request, @Valid @RequestBody LoanRequest loanRequest) {
        if (riskService.applyStatus(request, timeSupplier.getTime(), loanRequest.getAmount())) {
            return new ResponseEntity<>(new ApplicationResponse(REJECTED), HttpStatus.BAD_REQUEST);
        } else {
            loanService.applyForLoan(request, loanRequest);
            return new ResponseEntity<>(new ApplicationResponse(APPROVED), HttpStatus.OK);
        }
    }

    @PostMapping("/loans/{loan-id}/extend")
    public Loan extend(@PathVariable("loan-id") String id, @RequestParam(value = "days") Integer days) {
        return loanService.extend(id, days);
    }

}
