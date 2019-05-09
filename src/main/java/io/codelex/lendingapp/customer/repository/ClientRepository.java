package io.codelex.lendingapp.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT count(client)>0 FROM Client client where client.email =:email")
    boolean isEmailPresent(@Param("email") String email);

    Optional<Client> findByEmail(String email);

}
