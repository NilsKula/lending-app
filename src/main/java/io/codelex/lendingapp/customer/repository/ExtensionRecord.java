package io.codelex.lendingapp.customer.repository;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@NoArgsConstructor
@Entity
@Table(name = "extensions")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ExtensionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private LoanRecord loanRecord;
    private LocalDate created;
    private int days;
    private BigDecimal interest;

    public ExtensionRecord(LoanRecord loanRecord, LocalDate created, int days, BigDecimal interest) {
        this.loanRecord = loanRecord;
        this.created = created;
        this.days = days;
        this.interest = interest;
    }
}

