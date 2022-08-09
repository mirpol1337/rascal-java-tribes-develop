package VetulusJava.Tribes.Entities;

import VetulusJava.Tribes.Leaderboard.ILeaderboardable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@Entity
@DynamicUpdate
public class Troop implements ILeaderboardable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    int level;
    int hp;
    int attack;
    int defense;
    long startedAt;
    long finishedAt;
    boolean isInArmy;
    @ManyToOne
    @JsonIgnore   //nebo @ManyToOne(fetch = FetchType.LAZY)
    Kingdom kingdom;
    @ManyToOne
    @JsonIgnore
    Army army;

    public Troop(int level) {
        final int buildingTime = 60;
        this.level = level;
        hp = 10 * level;
        attack = level;
        defense = level;
        startedAt = System.currentTimeMillis() / 1000;
        finishedAt = startedAt + buildingTime;
    }

    public Troop(long id, int level, int hp, int attack, int defense, long startedAt, long finishedAt, Kingdom kingdom) {
        this.id = id;
        this.level = level;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.kingdom = kingdom;
    }

    public Troop() {
    }

    public Troop(int level, long startedAt, Kingdom kingdom) {
        this.level = level;
        this.hp = level * 10;
        this.attack = 1 * level;
        this.defense = 1 * level;
        this.startedAt = startedAt;
        this.kingdom = kingdom;
        this.finishedAt = startedAt + Constants.TROOP_TRAINING_TIME;
    }

    @Override
    public int getScore() {
        return this.level;
    }

    @Override
    public long getKingdomId() {
        return this.kingdom.getId();
    }
}
