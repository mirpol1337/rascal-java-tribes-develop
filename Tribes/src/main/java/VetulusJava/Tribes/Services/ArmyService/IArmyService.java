package VetulusJava.Tribes.Services.ArmyService;

import VetulusJava.Tribes.Entities.Army;
import VetulusJava.Tribes.Entities.Defenders;
import VetulusJava.Tribes.Entities.Kingdom;

import java.util.Collection;

public interface IArmyService {

    Collection<Army> getArmies(Kingdom kingdom);

    Army createAndSendArmy(Kingdom sourceKingdom, int troopsLevel, Kingdom targetKingdom);

    Defenders getDefenders(Long kingdomId, long currentTime);

    void fight();

    void disbandArmy();

    BattleOutcome calculateBattleOutcome(Defenders defenders, Army army);

    void loot(long lootingKingdomId, long lootedKingdomId, int survivingTroops);

    void razeBuilding(long KingdomId);
}
