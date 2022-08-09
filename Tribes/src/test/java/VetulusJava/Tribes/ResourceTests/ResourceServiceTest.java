package VetulusJava.Tribes.ResourceTests;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Entities.Resource;
import VetulusJava.Tribes.Enum.ResourceType;
import VetulusJava.Tribes.Repositories.IKingdomRepository;
import VetulusJava.Tribes.Repositories.IResourceRepository;
import VetulusJava.Tribes.Services.BuildingService.IBuildingService;
import VetulusJava.Tribes.Services.ResourceService.IResourceService;
import VetulusJava.Tribes.Services.ResourceService.ResourceService;
import VetulusJava.Tribes.Services.TroopService.ITroopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class ResourceServiceTest {
    private IResourceService resourceService;
    private IResourceRepository resourceRepository = mock(IResourceRepository.class);
    private IKingdomRepository kingdomRepository = mock(IKingdomRepository.class);
    private IBuildingService buildingService = mock(IBuildingService.class);
    private ITroopService troopService = mock (ITroopService.class);
    Kingdom kingdom = new Kingdom();
    Resource resource = new Resource(ResourceType.GOLD, 100, 1,kingdom);
    @BeforeEach
    void setUp(){
        resourceService = new ResourceService(resourceRepository,kingdomRepository,buildingService, troopService);
    }
    @Test
    public void findByType(){
        when(resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom)).thenReturn(resource);
        Resource actual = resourceService.findByTypeAndKingdom(ResourceType.GOLD, kingdom);
        assertEquals(ResourceType.GOLD,actual.getType());
        assertEquals(100,actual.getAmount());
        assertEquals(1,actual.getGeneration());
    }
    @Test
    public void findByTypeNull(){
        when(resourceRepository.findByType(ResourceType.FOOD)).thenReturn(null);
        Resource actual = resourceService.findByTypeAndKingdom(ResourceType.FOOD, kingdom);
        assertNull(actual);
    }
}