package VetulusJava.Tribes.Entities;

import VetulusJava.Tribes.Enum.BuildingType;
import VetulusJava.Tribes.Leaderboard.ILeaderboardable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
public class Building implements ILeaderboardable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    public long ID;
    @Enumerated(EnumType.STRING)
    public BuildingType type;
    public int level;
    public int hp;
    public long started_at;
    public long finished_at;
    @ManyToOne
    @JsonIgnore
    public Kingdom kingdom;

    public Building() {

    }

    public Building(BuildingType type, int level, int hp, Long started_at, Long finished_at) {
        this.type = type;
        this.level = level;
        this.hp = hp;
        this.started_at = started_at;
        this.finished_at = finished_at;
    }

    public Building(BuildingType type, int level, int hp, Long started_at, Long finished_at, Kingdom kingdom) {
        this.type = type;
        this.level = level;
        this.hp = hp;
        this.started_at = started_at;
        this.finished_at = finished_at;
        this.kingdom = kingdom;
    }

    @Override
    public int getScore() {
        return this.level;
    }

    @Override
    public long getKingdomId() {
        return this.kingdom.getId();
    }

    public int getStorage() {
        return level * Constants.BUILDING_STORAGE;
    }

    public int getGoldGeneration() {
        return level * Constants.BUILDING_GOLD_GENERATION;
    }

    public int getFoodGeneration() {
        return level * Constants.BUILDING_FOOD_GENERATION;
    }
}