package io.codelex.lendingapp.customer;

import io.codelex.lendingapp.customer.api.LoanRequest;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class InterestServiceTest {

    private InterestService interestService = new InterestService();

    @Test
    public void should_return_initial_loans_interest() {
        //given
        LoanRequest loanRequest = new LoanRequest(new BigDecimal("500.0"), 7);
        //when
        BigDecimal result = interestService.calculate(loanRequest.getAmount(), loanRequest.getDays());
        //then
        Assert.assertEquals(new BigDecimal("11.67"), result);
    }

    @Test
    public void should_calculate_extensions_interest() {
        //when
        BigDecimal result = interestService.calculateExtension(new BigDecimal("550.0"), 7);
        //then
        Assert.assertEquals(new BigDecimal("12.83"), result);
    }
}