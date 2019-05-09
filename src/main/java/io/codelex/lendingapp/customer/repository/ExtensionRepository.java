package io.codelex.lendingapp.customer.repository;

import io.codelex.lendingapp.customer.api.Extension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExtensionRepository extends JpaRepository<ExtensionRecord, String> {

    List<Extension> findAllByLoanRecord(LoanRecord loanRecord);
}
