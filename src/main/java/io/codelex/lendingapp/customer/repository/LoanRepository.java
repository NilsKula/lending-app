package io.codelex.lendingapp.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<LoanRecord, Long> {

    @Query("SELECT count(client)>0 FROM LoanRecord loanRecord WHERE loanRecord.client =:client")
    boolean isLoanPresent(@Param("client") Client client);

    @Query("SELECT count(loanRecord)>1 FROM LoanRecord loanRecord "
            + "WHERE loanRecord.ip =:ip "
            + "AND loanRecord.created between :validation AND :created "
            + "AND loanRecord.principal=:principal")
    boolean ipRiskAssessmentValidation(@Param("ip") String ip,
                                       @Param("created") LocalDateTime created,
                                       @Param("validation") LocalDateTime validation,
                                       @Param("principal") BigDecimal principal);

    Optional<LoanRecord> findAllByClient(Client client);

    LoanRecord findByLoanId(String id);

}
