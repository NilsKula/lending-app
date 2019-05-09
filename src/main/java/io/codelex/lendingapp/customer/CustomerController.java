package io.codelex.lendingapp.customer;

import io.codelex.lendingapp.customer.api.AuthRequest;
import io.codelex.lendingapp.customer.api.Constraints;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/register")
    public void register(@Valid @RequestBody AuthRequest authRequest) {
        customerService.register(authRequest);
    }

    @PostMapping("/sign-in")
    public void signIn(@Valid @RequestBody AuthRequest authRequest) {
        customerService.signIn(authRequest);
    }

    @PostMapping("/sign-out")
    public void signOut() {
        customerService.signOut();
    }

    @GetMapping("/constraints")
    public Constraints checkConstraints() {
        return customerService.constraints();
    }

}



