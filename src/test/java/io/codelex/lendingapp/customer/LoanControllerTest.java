package io.codelex.lendingapp.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.codelex.lendingapp.TimeSupplier;
import io.codelex.lendingapp.customer.api.Loan;
import io.codelex.lendingapp.customer.api.LoanRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoanController.class)
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    HttpServletRequest httpServletRequest;
    @MockBean
    LoanService loanService;
    @MockBean
    RiskService riskService;
    @MockBean
    TimeSupplier timeSupplier;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(
                LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        javaTimeModule.addDeserializer(
                LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        javaTimeModule.addSerializer(
                LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );
        javaTimeModule.addSerializer(
                LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        builder.modules(javaTimeModule);
        builder.featuresToDisable(WRITE_DATES_AS_TIMESTAMPS);

        MAPPER.registerModule(javaTimeModule);
    }

    @Test
    @WithMockUser(username = "user", password = "qwerty", roles = "CUSTOMER")
    public void should_be_able_to_apply_for_a_loan() throws Exception {


        LoanRequest loanRequest = new LoanRequest(
                new BigDecimal("500.0"),
                30
        );

        String json = MAPPER.writeValueAsString(loanRequest);

        mockMvc.perform(
                post("/api/loans/apply")
                        .content(json)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").value("APPROVED"));

    }

    @Test
    @WithMockUser(username = "user", password = "qwerty", roles = "CUSTOMER")
    public void should_reject_the_apply_for_loan_if_risk_service_fails_to_pass() throws Exception {

        LoanRequest loanRequest = new LoanRequest(
                new BigDecimal("500.0"),
                30
        );

        String json = MAPPER.writeValueAsString(loanRequest);

        Mockito.when(
                riskService.applyStatus(
                        any(),
                        any(),
                        any()))
                .thenReturn(true);
        mockMvc.perform(
                post("/api/loans/apply")
                        .content(json)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*").value("REJECTED"));

    }

    @Test
    @WithMockUser(username = "user", password = "qwerty", roles = "CUSTOMER")
    public void should_return_loan() throws Exception {

        Loan loan = new Loan(
                "0000-0000",
                "OPEN",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                new BigDecimal("500.0"),
                new BigDecimal("50.0"),
                new BigDecimal("550.0"),
                new ArrayList<>()
        );

        Mockito.lenient()
                .when(loanService.loans())
                .thenReturn(Collections.singletonList(loan));

        mockMvc.perform(get("/api/loans"))
                .andExpect(jsonPath("$.[0].total").value("550.0"));

    }
}