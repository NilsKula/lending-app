package io.codelex.lendingapp.customer.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Loan {

    private final String id;
    private final String status;
    private final LocalDate created;
    private final LocalDate dueDate;
    private final BigDecimal principal;
    private final BigDecimal interest;
    private final BigDecimal total;
    private final List<Extension> extensions;

}
