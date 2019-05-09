package io.codelex.lendingapp.customer;

import io.codelex.lendingapp.TimeSupplier;
import io.codelex.lendingapp.customer.api.LoanRequest;
import io.codelex.lendingapp.customer.repository.LoanRepository;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class RiskServiceTest {

    private LoanRepository loanRepository = mock(LoanRepository.class);
    private TimeSupplier timeSupplier = spy(TimeSupplier.class);
    private RiskService riskService = new RiskService(timeSupplier, loanRepository);
    private HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

    @Test
    public void should_not_be_able_to_apply_for_max_amount_at_night() {
        //given
        LoanRequest loanRequest = new LoanRequest(new BigDecimal("500.0"), 30);
        //when
        timeSupplier.setTime(LocalDate.of(2019, 1, 1).atTime(0, 0));
        boolean result = riskService.applyTimeRiskEvaluation(loanRequest.getAmount());
        //then
        assertTrue(result);
    }

    @Test
    public void should_not_be_able_to_apply_for_three_or_more_max_amount_loans_from_same_ip() {
        //given
        timeSupplier.setTime(LocalDateTime.now());
        when(httpServletRequest.getHeader("X-FORWARDED-FOR")).thenReturn("577.261.217.278");
        when(loanRepository.ipRiskAssessmentValidation(
                httpServletRequest.getHeader("X-FORWARDED-FOR"),
                timeSupplier.getTime(),
                timeSupplier.getTime().minusHours(24),
                new BigDecimal("500.0"))).thenReturn(true);
        //when
        boolean result = riskService.ipRiskEvaluation(httpServletRequest, timeSupplier.getTime());
        //then
        assertTrue(result);
    }
}
