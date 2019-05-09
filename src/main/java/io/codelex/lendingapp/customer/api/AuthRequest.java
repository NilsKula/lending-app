package io.codelex.lendingapp.customer.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class AuthRequest {

    @Email
    @NotEmpty
    private final String email;

    @NotEmpty
    @Size(min = 6)
    private final String password;

}
