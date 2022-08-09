package VetulusJava.Tribes.KingdomTests;

import VetulusJava.Tribes.Entities.Kingdom;

import VetulusJava.Tribes.Repositories.IKingdomRepository;
import VetulusJava.Tribes.Services.BuildingService.IBuildingService;
import VetulusJava.Tribes.Services.KingdomService.IKingdomService;
import VetulusJava.Tribes.Services.KingdomService.KingdomService;
import VetulusJava.Tribes.Services.ResourceService.IResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
public class KingdomServiceTest {
    private IKingdomService kingdomService;
    private IKingdomRepository kingdomRepository = mock(IKingdomRepository.class);
    private IResourceService resourceService;
    private IBuildingService buildingService;

    Kingdom kingdom = new Kingdom("Winterfell", 1,1);
    Kingdom kingdom2 = new Kingdom("Kingdom", 2,2);

    @BeforeEach
    void setUp(){
        kingdomService = new KingdomService(kingdomRepository, resourceService, buildingService);
    }

    @Test
    public void kingdomFound(){
        when(kingdomRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(kingdom2));
        Kingdom actual = kingdomService.getById(1);
        assertEquals(kingdom2, actual);
    }

    @Test
    public void kingdomNotFound(){
        when(kingdomRepository.findById(2L)).thenReturn(Optional.ofNullable(null));
        var actual = kingdomService.getById(2);
        assertNull(actual);
    }

    @Test  //pridano 22/6
    @DisplayName("find kingdom by user id")
    public void findKingdomByUserId(){
        UUID uuid = UUID.randomUUID();
        when(kingdomRepository.findKingdomByUser_Id(uuid)).thenReturn(kingdom2);
        var actual = kingdomService.getByUserId(uuid);
        assertEquals(kingdom2, actual);
    }
}
