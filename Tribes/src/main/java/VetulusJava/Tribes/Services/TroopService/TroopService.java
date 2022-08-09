package VetulusJava.Tribes.Services.TroopService;


import VetulusJava.Tribes.Entities.Constants;
import VetulusJava.Tribes.Entities.Defenders;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Entities.Troop;
import VetulusJava.Tribes.Enum.BuildingType;
import VetulusJava.Tribes.Repositories.ITroopRepository;
import VetulusJava.Tribes.Services.BuildingService.IBuildingService;
import VetulusJava.Tribes.Services.BuyService.BuyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TroopService implements ITroopService {

    private ITroopRepository troopRepository;
    private IBuildingService buildingService;

    public TroopService(ITroopRepository troopRepository, IBuildingService buildingService) {
        this.troopRepository = troopRepository;
        this.buildingService = buildingService;
    }

    public Troop getById(long id) {
        var troop = troopRepository.findById(id);
        return troop.isPresent() ? troop.get() : null;
    }

    private int getBarracksLevel(Kingdom kingdom) {
        return buildingService.getByTypeAndKingdomId(BuildingType.BARRACKS, kingdom.getId()).getLevel();
    }

    private Collection<Troop> getPendingTroops(Long kingdomId, long currentTime) {
        return troopRepository.findTroopsByFinishedAtGreaterThanAndKingdomId(kingdomId, currentTime);
    }

    private long getLastFinishedAtTime(Collection<Troop> pendingTroops) {
        return pendingTroops.stream().max(Comparator.comparing(Troop::getFinishedAt)).orElseThrow(NoSuchElementException::new).getFinishedAt();
    }

    private Troop addOrUpdateTroop(Troop troop) {
        return troopRepository.save(troop);
    }

    @Override
    public Collection<Troop> getTroops(Long kingdomId) {
        return troopRepository.findAllByKingdomId(kingdomId);
    }

    private BuyResponse getTroopForUpdate(int requestedLevel, Kingdom kingdom, BuyResponse buyResponse) {
        Optional<Troop> troop = troopRepository.findFirstByLevelAndKingdomId(requestedLevel - 1, kingdom.getId());
        if (!troop.isPresent()) {
            buyResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            buyResponse.setMessage(String.format("No troop of level %s found", requestedLevel - 1));
            return buyResponse;
        }
        buyResponse.setTroop(troop.get());
        return buyResponse;
    }

    private Troop levelUp(Troop troopToUpdate, long startedAtTime) {
        troopToUpdate.setStartedAt(startedAtTime);
        troopToUpdate.setFinishedAt((startedAtTime + Constants.TROOP_TRAINING_TIME));
        troopToUpdate.setLevel(troopToUpdate.getLevel() + 1);
        troopToUpdate.setAttack(troopToUpdate.getAttack() + 1);
        troopToUpdate.setDefense(troopToUpdate.getDefense() + 1);
        return troopToUpdate;
    }

    @Override
    public void starveTroops(int count, Kingdom kingdom) {
        Iterable<Troop> starvingTroops = troopRepository.findTopByLevelAndKingdomId(count, kingdom.getId()); //get starving troops. Lower level ones starve first.
        System.out.println(String.format("%d soldiers starved to death", count));
        troopRepository.deleteAll(starvingTroops);
    }

    @Override
    public BuyResponse createOrUpdateTroop(Integer requestedLevel, Kingdom kingdom, BuyResponse buyResponse) {
        long currentTime = System.currentTimeMillis() / 1000;
        if (requestedLevel != null) {
            buyResponse = getTroopForUpdate(requestedLevel, kingdom, buyResponse); // find troop with level = requestedLevel-1
            if (buyResponse.getHttpStatus() == HttpStatus.BAD_REQUEST) {
                return buyResponse;
            }
        }
        Collection<Troop> pendingTroops = getPendingTroops(kingdom.getId(), currentTime);
        if (pendingTroops.size() >= Constants.MAX_QUEUE_LENGTH) {
            buyResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            buyResponse.setMessage("Max queue length is 5");
            return buyResponse;
        }
        long startedAtTime = pendingTroops.isEmpty() ? currentTime : getLastFinishedAtTime(pendingTroops);
        buyResponse.setHttpStatus(HttpStatus.OK);
        if (buyResponse.getTroop() == null) {
            Troop newTroop = new Troop(getBarracksLevel(kingdom), startedAtTime, kingdom);
            buyResponse.setTroop(addOrUpdateTroop(newTroop));
        } else {
            buyResponse.setTroop(addOrUpdateTroop(levelUp(buyResponse.getTroop(), startedAtTime)));
        }
        return buyResponse;
    }
}
