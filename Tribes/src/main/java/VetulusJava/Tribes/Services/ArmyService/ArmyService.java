package VetulusJava.Tribes.Services.ArmyService;

import VetulusJava.Tribes.Entities.*;
import VetulusJava.Tribes.Enum.ResourceType;
import VetulusJava.Tribes.Exceptions.NotFoundException;
import VetulusJava.Tribes.Repositories.IArmyRepository;
import VetulusJava.Tribes.Repositories.IBuildingRepository;
import VetulusJava.Tribes.Repositories.ITroopRepository;
import VetulusJava.Tribes.Services.BuildingService.IBuildingService;
import VetulusJava.Tribes.Services.KingdomService.IKingdomService;
import VetulusJava.Tribes.Services.ResourceService.IResourceService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArmyService implements IArmyService {

    private IArmyRepository armyRepository;
    private ITroopRepository troopRepository;
    private IKingdomService kingdomService;
    private IResourceService resourceService;
    private IBuildingService buildingService;

    public ArmyService(IArmyRepository armyRepository, ITroopRepository troopRepository, IKingdomService kingdomService, IResourceService resourceService, IBuildingService buildingService) {
        this.armyRepository = armyRepository;
        this.troopRepository = troopRepository;
        this.kingdomService = kingdomService;
        this.resourceService = resourceService;
        this.buildingService = buildingService;
    }

    @Override
    public Collection<Army> getArmies(Kingdom kingdom) {
        return armyRepository.findAllByKingdomId(kingdom.getId());
    }

    @Override
    public Army createAndSendArmy(Kingdom sourceKingdom, int troopsLevel, Kingdom targetKingdom) {
        long currentTime = System.currentTimeMillis() / 1000;
        Set<Troop> troops = getTroopsToSend(sourceKingdom, troopsLevel, currentTime);
        if (troops == null || troops.isEmpty()) {
            throw new NotFoundException("No troops on this level!");
        }
        long travelTime = kingdomService.calculateTravelTime(kingdomService.calculateDistance(sourceKingdom, targetKingdom));
        long arrivalAt = currentTime + travelTime;
        Army army = new Army(sourceKingdom, troops, currentTime, arrivalAt, targetKingdom.getId(), getTotalHP(troops), getTotalDefense(troops), getTotalAttack(troops));
        armyRepository.save(army);
        putTroopsToArmy(troops, army);  //change status to inArmy for enlisted troops
        return army;
    }

    public Set<Troop> getTroopsToSend(Kingdom kingdom, int level, long currentTime) {
        return troopRepository.findAllByLevelAndKingdomIdAndInArmyAndFinishedAtBefore(level, kingdom.getId(), false, currentTime);
    }

    public int getTotalHP(Set<Troop> troops) {
        return troops.stream().mapToInt(t -> t.getHp()).sum();
    }

    public int getTotalDefense(Set<Troop> troops) {
        return troops.stream().mapToInt(t -> t.getDefense()).sum();
    }

    public int getTotalAttack(Set<Troop> troops) {
        return troops.stream().mapToInt(t -> t.getAttack()).sum();
    }

    private void putTroopsToArmy(Set<Troop> troops, Army army) {
        for (Troop troop : troops) {
            troop.setInArmy(true);
            troop.setArmy(army);
        }
        troopRepository.saveAll(troops);
    }

    @Override
    public Defenders getDefenders(Long kingdomId, long currentTime) {
        Set<Troop> troops = troopRepository.findAllByKingdomIdAndInArmyAndFinishedAtBefore(kingdomId, false, currentTime);
        if (troops == null || troops.isEmpty()) {
            return new Defenders();
        }
        return new Defenders(getTotalHP(troops), getTotalAttack(troops), getTotalDefense(troops), troops.size(), troops);
    }

    //find all armies that have arrived and started fighting
    @Override
    public void fight() {
        long currentTime = System.currentTimeMillis() / 1000;
        Set<Army> armiesGoingToBattle = armyRepository.findAllByArrivedAtBeforeAndGoingToBattle(currentTime);
        for (Army army : armiesGoingToBattle) {
            System.out.println("Army is fighting");
            Defenders defenders = getDefenders(army.getTargetKingdomId(), currentTime);
            System.out.println(calculateBattleOutcome(defenders, army));
        }
    }

    //Calculate the ratio of "strength" of the fighting sides. Then subtract troops to both sides inversely proportional to the ratio
    //i.e. half the strength = twice the losses. Half of all fighting soldiers die in battle.
    //the method calculates the losses, removes dead troops from both sides and returns the battle outcome
    @Override
    public BattleOutcome calculateBattleOutcome(Defenders defenders, Army army) {
        double defendersStrength = defenders.getTotalHP() - (army.getTotalAttack() - defenders.getTotalDefense());
        army.setGoingToBattle(false);  //army is on its way home from now on
        armyRepository.save(army);  //army is marching home
        int armySize = army.getTroops().size();
        if (defendersStrength <= 0) {
            loot(army.getKingdom().getId(), army.getTargetKingdomId(), armySize);
            return BattleOutcome.Victory;
        }
        double armyStrength = army.getTotalHP() - (defenders.getTotalAttack() - army.getTotalDefense());
        double totalStrength = defendersStrength + armyStrength;
        double armyStrengthRatio = armyStrength / totalStrength;
        double defendersStrengthRatio = defendersStrength / totalStrength;
        double baseLoss = 0.5;                                           // half of all soldiers die in battle
        double totalPredictedLoss = (defenders.getCount() + armySize) * baseLoss;  // doesn't work well for great disproportions. 1 attacker against 10 defenders will result in 1 loss on both sides
        int defendersLosses = (int) Math.round(totalPredictedLoss * (1 - defendersStrengthRatio));
        int armyLosses = (int) Math.round(totalPredictedLoss * (1 - armyStrengthRatio));
        army = removeDeadArmyTroops(army, armyLosses);
        removeDeadDefenders(defenders, defendersLosses);
        if (armyLosses >= armySize) {  //in case the army is destroyed it is removed right away
            armyRepository.delete(army.getId());
            return BattleOutcome.Loss;
        }
        //armyRepository.save(army);  // save modified army = reduced in size and marching home
        //return armyStrength > defendersStrength ? BattleOutcome.Victory : BattleOutcome.Loss;
        if (armyStrength > defendersStrength){
            loot(army.getKingdom().getId(), army.getTargetKingdomId(), armySize-armyLosses);
            return BattleOutcome.Victory;
        }
        return BattleOutcome.Loss;
    }

    public Army removeDeadArmyTroops(Army army, int count) {
        Collection<Troop> deadTroops = army.getTroops().stream().limit(count).collect(Collectors.toCollection(ArrayList<Troop>::new));
        army.getTroops().removeAll(deadTroops);     //remove dead troops from army object
        troopRepository.deleteAll(deadTroops);      // remove them from DB
        //armyRepository.save(army);   // na tomhle to ztroskota. Asi proto, ze se napred vytvori SELECT prikaz co hleda (mezitim smazaneho) vojaky
        System.out.println(String.format("%d dead invaders", count));
        return army;
    }

    //lower level defenders dies first
    public void removeDeadDefenders(Defenders defenders, int count) {
        Collection<Troop> deadDefenders = defenders.getTroops().stream().sorted(Comparator.comparingInt(Troop::getLevel)).limit(count).collect(Collectors.toCollection(ArrayList<Troop>::new));
        troopRepository.deleteAll(deadDefenders);
        System.out.println(String.format("%d dead defenders", count));
    }

    //disband armies that have returned home and release troops
    @Override
    public void disbandArmy() {
        Set<Army> returningArmies = armyRepository.findAllByReturnedAtBefore(System.currentTimeMillis() / 1000);
        for (Army army : returningArmies) {
            Collection<Troop> returningTroops = army.getTroops();
            for (Troop troop : returningTroops) {   // troops are no longer in army
                troop.setInArmy(false);
                troop.setArmy(null);
            }
            troopRepository.saveAll(returningTroops);
            armyRepository.delete(army.getId());
            System.out.println(String.format("Army %d disbanded", army.getId()));
        }
    }

    @Override
    public void loot(long lootingKingdomId, long lootedKingdomId, int survivingTroops) {
        int maxLootedResources = survivingTroops * Constants.TROOP_LOOT_CAPACITY;
        int lootedGold = resourceService.lootResource(lootedKingdomId, maxLootedResources, ResourceType.GOLD);
        int lootedFood = resourceService.lootResource(lootedKingdomId, maxLootedResources, ResourceType.FOOD);
        resourceService.addResource(lootingKingdomId, lootedGold, ResourceType.GOLD);
        resourceService.addResource(lootingKingdomId, lootedFood, ResourceType.FOOD);
        System.out.println(kingdomService.getById(lootingKingdomId).getName()+ " looted "
                + lootedGold + " gold & "
                + lootedFood + " food from "
                + kingdomService.getById(lootedKingdomId).getName());
        //when looting occurs, there is a chance for a building being damaged (it loses one level)
        razeBuilding(lootedKingdomId);
    }

    public void razeBuilding(long KingdomId) {
        Random rng = new Random();
        if (rng.nextBoolean()){
            buildingService.decreaseBuildingLevel(KingdomId);
        }
    }
}