package VetulusJava.Tribes.Entities;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public class Defenders {
    private int totalHP;
    private int totalAttack;
    private int totalDefense;
    private int count;
    private Long kingdomId;
    private Collection<Troop> troops;

    public Defenders(int totalHP, int totalAttack, int totalDefense, int count, Collection<Troop> troops) {
        this.totalHP = totalHP;
        this.totalAttack = totalAttack;
        this.totalDefense = totalDefense;
        this.count = count;
        this.troops = troops;
    }

    public Defenders() {
    this.troops = new ArrayList<Troop>();
    }
}
