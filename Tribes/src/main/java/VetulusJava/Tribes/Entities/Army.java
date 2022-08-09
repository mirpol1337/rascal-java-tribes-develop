package VetulusJava.Tribes.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class Army {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    private int totalHP;
    private int totalAttack;
    private int totalDefense;
    private long startedAt;
    private long arrivedAt;
    private long returnedAt;
    private boolean goingToBattle;
    @ManyToOne
    @JsonIgnore
    Kingdom kingdom;
    private Long targetKingdomId;
    @OneToMany(mappedBy = "army", fetch = FetchType.EAGER)
    private Set<Troop> troops;

    public Army() {
    }

    public Army(Kingdom kingdom, Set<Troop> troops, long startedAt, long arrivedAt, Long targetKingdomId, int totalHP, int totalDefense, int totalAttack) {
        this.kingdom = kingdom;
        this.troops = troops;
        this.startedAt = startedAt;
        this.arrivedAt = arrivedAt;
        this.targetKingdomId = targetKingdomId;
        this.totalHP = totalHP;
        this.totalDefense = totalDefense;
        this.totalAttack = totalAttack;
        this.goingToBattle = true;
        this.returnedAt = System.currentTimeMillis() / 1000 + 2 * (arrivedAt - startedAt);
    }

}
