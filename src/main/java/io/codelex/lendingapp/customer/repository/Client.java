package io.codelex.lendingapp.customer.repository;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "clients")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    public Client(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
