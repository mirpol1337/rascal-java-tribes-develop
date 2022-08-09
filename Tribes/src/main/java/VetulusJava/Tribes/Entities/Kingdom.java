package VetulusJava.Tribes.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Kingdom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int x;
    private int y;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private ApplicationUser user;
    @OneToMany(mappedBy = "kingdom",fetch = FetchType.EAGER)
    private Set<Building> buildings;
    @OneToMany(mappedBy = "kingdom",fetch = FetchType.EAGER)
    private Set<Troop> troops;
    @OneToMany(mappedBy = "kingdom", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Resource> resources;
    @OneToMany(mappedBy = "kingdom", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Army> armies;

    public Kingdom() {
    }

    public Kingdom(Long id) {
        this.id = id;
    }

    public Kingdom(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public Kingdom(String name, ApplicationUser user) {
        this.name = name;
        this.user = user;
    }
}
