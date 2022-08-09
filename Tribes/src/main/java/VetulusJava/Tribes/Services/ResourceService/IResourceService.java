package VetulusJava.Tribes.Services.ResourceService;

import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Entities.Resource;
import VetulusJava.Tribes.Enum.ResourceType;


public interface IResourceService {
    Iterable<Resource> findAllResources();

    Resource findByTypeAndKingdom(ResourceType type, Kingdom kingdom);

    void setStartingResources(Kingdom kingdom);

    void calculateProduction();

    int lootResource(long lootedKingdomId, int maxLootedResources, ResourceType resourceType);

    void addResource(long kingdomId, int amount, ResourceType resourceType);
}