package io.codelex.lendingapp.customer.repository;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "loans")
@Getter
@Setter
@EqualsAndHashCode(of = "loanId")
public class LoanRecord {

    @Id
    @GenericGenerator(name = "loan_id", strategy = "io.codelex.lendingapp.customer.repository.PrefixedIdGenerator")
    @GeneratedValue(generator = "loan_id")
    private String loanId;
    private String loanStatus;
    private LocalDateTime created;
    private LocalDate dueDate;
    private Integer initialRequestDays;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal total;
    private String ip;

    @ManyToOne
    private Client client;

}