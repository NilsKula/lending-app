package io.codelex.lendingapp.customer;

import io.codelex.lendingapp.customer.api.AuthRequest;
import io.codelex.lendingapp.customer.api.Constraints;
import io.codelex.lendingapp.customer.repository.Client;
import io.codelex.lendingapp.customer.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CustomerService {

    private final AuthService authService;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    void register(AuthRequest authRequest) {
        if (clientRepository.isEmailPresent(authRequest.getEmail().toLowerCase().trim())) {
            throw new InvalidStatusException();
        }
        authService.authorise(authRequest.getEmail(), "ROLE_CUSTOMER");

        clientRepository.save(new Client(
                authRequest.getEmail().toLowerCase().trim(),
                passwordEncoder.encode(authRequest.getPassword())));
    }

    Constraints constraints() {
        return new Constraints(
                new BigDecimal("100.0"),
                new BigDecimal("500.0"),
                7,
                30,
                7,
                30
        );
    }

    void signIn(AuthRequest authRequest) {
        Client client = clientRepository.findByEmail(authRequest.getEmail().toLowerCase().trim()).orElseThrow(InvalidStatusException::new);

        if (!passwordEncoder.matches(authRequest.getPassword(), client.getPassword())) {
            throw new InvalidRequestException();
        }
        authService.authorise(authRequest.getEmail(), "ROLE_CUSTOMER");
    }

    void signOut() {
        authService.clearAuthentication();
    }
}
