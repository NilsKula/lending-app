package io.codelex.lendingapp.customer;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static java.math.RoundingMode.*;

@Component
public class InterestService {
    private static final BigDecimal INTEREST_RATE_PER_MONTH = new BigDecimal("0.1");
    private static final BigDecimal EXTENSION_INTEREST_COEFF = new BigDecimal("0.1");
    private static final int DAYS_IN_MONTH = 30;

    BigDecimal calculate(BigDecimal principal, Integer days) {
        var monthlyInterest = principal.setScale(3, HALF_DOWN).multiply(INTEREST_RATE_PER_MONTH);
        return monthlyInterest
                .divide(new BigDecimal(DAYS_IN_MONTH).setScale(3, HALF_DOWN), DOWN)
                .multiply(new BigDecimal(days))
                .setScale(2, HALF_DOWN);
    }

    BigDecimal calculateExtension(BigDecimal initialTotal, Integer days) {
        var initialSum = initialTotal.setScale(3, HALF_DOWN).multiply(EXTENSION_INTEREST_COEFF);
        return initialSum
                .divide(new BigDecimal(DAYS_IN_MONTH).setScale(3, HALF_DOWN), DOWN)
                .multiply(new BigDecimal(days))
                .setScale(2, HALF_DOWN);
    }
}