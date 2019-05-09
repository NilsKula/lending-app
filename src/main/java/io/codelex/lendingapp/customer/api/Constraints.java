package io.codelex.lendingapp.customer.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class Constraints {

    private final BigDecimal minAmount;
    private final BigDecimal maxAmount;
    private final int minTermDays;
    private final int maxTermDays;
    private final int minExtensionDays;
    private final int maxExtensionDays;

}
