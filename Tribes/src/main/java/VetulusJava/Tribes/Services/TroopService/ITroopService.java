package VetulusJava.Tribes.Services.TroopService;

import VetulusJava.Tribes.Entities.Defenders;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Entities.Troop;
import VetulusJava.Tribes.Services.BuyService.BuyResponse;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collector;

public interface ITroopService {
    Troop getById(long id);

    Collection<Troop> getTroops(Long kingdomId);

    void starveTroops(int count, Kingdom kingdom);

    BuyResponse createOrUpdateTroop(Integer requestedLevel, Kingdom kingdom, BuyResponse buyResponse);
}
