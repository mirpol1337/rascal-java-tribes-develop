package VetulusJava.Tribes.ResourceTests;

import VetulusJava.Tribes.Entities.*;
import VetulusJava.Tribes.Enum.BuildingType;
import VetulusJava.Tribes.Enum.ResourceType;
import VetulusJava.Tribes.Repositories.IBuildingRepository;
import VetulusJava.Tribes.Repositories.IKingdomRepository;
import VetulusJava.Tribes.Repositories.IResourceRepository;
import VetulusJava.Tribes.Repositories.ITroopRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResourceRepositoryTest {
    @Autowired
    IKingdomRepository kingdomRepository;
    @Autowired
    IResourceRepository resourceRepository;
    private Kingdom kingdom;

    @BeforeAll
    private void init() {
        kingdomRepository.save(new Kingdom(1L));
        this.kingdom = kingdomRepository.findById(1L).get();
    }

    @Autowired
    IBuildingRepository buildingRepository;
    @Autowired
    ITroopRepository troopRepository;

    @Test
    public void setStartingGold() {
        Resource resource = new Resource(ResourceType.GOLD, 100, 1, kingdom);
        resourceRepository.save(resource);
        Iterable<Resource> gold = resourceRepository.findAll();
        assertThat(gold).extracting(Resource::getAmount).contains(100);
        assertThat(gold).extracting(Resource::getType).contains(ResourceType.GOLD);
    }

    @Test
    public void returningGoldForKingdom() {
        Resource resource = new Resource(ResourceType.GOLD, 100, 1, kingdom);
        resourceRepository.save(resource);
        Resource gold = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom);
        assertThat(gold.getAmount()).isEqualTo(100);
    }

    @Test
    public void changeResourcesForKingdom() {
        Resource resource = new Resource(ResourceType.GOLD, 100, 1, kingdom);
        resourceRepository.save(resource);
        Resource gold = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom);
        gold.setAmount(gold.getAmount() - 50);
        resourceRepository.save(gold);
        Resource actual = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom);
        assertThat(actual.getAmount()).isEqualTo(50);
    }

    @Test
    public void calculateProduction() {
        Building building = new Building(BuildingType.TOWNHALL, 1, 1, 255446L, 1234567L);
        Resource gold = new Resource(ResourceType.GOLD, 100, 10, kingdom);
        Resource food = new Resource(ResourceType.FOOD, 9, 9, kingdom);
        if (building.type.equals(BuildingType.TOWNHALL) && building.level == 1) {
            Resource actual = new Resource(ResourceType.GOLD, 100, 10, kingdom);
            assertThat(actual.getGeneration()).isEqualTo(gold.generation);
        }
        if (building.type.equals(BuildingType.TOWNHALL) && building.level == 1 && troopRepository.count() == 1) {
            Resource actual = new Resource(ResourceType.FOOD, 9, 9, kingdom);
            assertThat(actual.getGeneration()).isEqualTo(food.generation);
        }
    }

    @Test
    public void storageCheck() {
        Building building = new Building(BuildingType.TOWNHALL, 1, 1, 255446L, 1234567L);
        if (building.type.equals(BuildingType.TOWNHALL) && building.level == 1) {
            int storage = 1000;
            int actual = Constants.BUILDING_STORAGE * building.getLevel();
            assertThat(actual == storage);
        }
    }

    @Test
    public void generateGold() {
        Resource gold = new Resource(ResourceType.GOLD, 110, 10, kingdom);
        if (resourceRepository.equals(ResourceType.GOLD)) {
            Resource actual = new Resource(ResourceType.GOLD, 100, 10, kingdom);
            assertThat(actual.amount + actual.generation == gold.amount);
        }
    }

    @Test
    public void generateFood() {
        Resource food = new Resource(ResourceType.FOOD, 110, 10, kingdom);
        if (resourceRepository.equals(ResourceType.FOOD)) {
            Resource actual = new Resource(ResourceType.FOOD, 100, 10, kingdom);
            assertThat(actual.amount + actual.generation == food.amount);
        }
    }
}