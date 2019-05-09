package io.codelex.lendingapp.customer;

import io.codelex.lendingapp.TimeSupplier;
import io.codelex.lendingapp.customer.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class RiskService {

    private final TimeSupplier timeSupplier;
    private final LoanRepository loanRepository;

    boolean applyStatus(HttpServletRequest request, LocalDateTime created, BigDecimal amount) {
        return applyTimeRiskEvaluation(amount) || ipRiskEvaluation(request, created);
    }

    boolean applyTimeRiskEvaluation(BigDecimal amount) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
        String s = "20:59";
        String e = "04:01";
        String t = format.format(timeSupplier.getTime());
        LocalTime nightStart = LocalTime.parse(s, format);
        LocalTime nightEnd = LocalTime.parse(e, format);
        LocalTime targetTime = LocalTime.parse(t, format);

        return !(targetTime.isAfter(nightEnd) && targetTime.isBefore(nightStart))
                && amount.equals(new BigDecimal("500.0"));
    }

    boolean ipRiskEvaluation(HttpServletRequest request, LocalDateTime created) {
        final String userIp = request.getHeader("X-FORWARDED-FOR");
        return loanRepository.ipRiskAssessmentValidation(userIp, created, created.minusHours(24), new BigDecimal("500.0"));
    }
}
