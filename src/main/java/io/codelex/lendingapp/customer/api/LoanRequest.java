package io.codelex.lendingapp.customer.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class LoanRequest {

    @NotNull
    @Range(min = 100, max = 500)
    private final BigDecimal amount;

    @NotNull
    @Range(min = 7, max = 30)
    private final Integer days;

}
