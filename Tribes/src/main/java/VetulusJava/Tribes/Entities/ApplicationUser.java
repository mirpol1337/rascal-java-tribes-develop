package VetulusJava.Tribes.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@Entity
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    @OneToOne(cascade=CascadeType.ALL)
    private Kingdom kingdom;
    private String password;
    private String email;

    public ApplicationUser(String name, String password, Kingdom kingdom, String email) {
        this.name = name;
        this.password = password;
        this.kingdom = kingdom;
        this.email = email;
    }

    public ApplicationUser(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public ApplicationUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public ApplicationUser() {
    }
}