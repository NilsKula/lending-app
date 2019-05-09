package io.codelex.lendingapp.customer.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class Extension {

    private final LocalDate created;
    private final int days;
    private final BigDecimal interest;
    
}
