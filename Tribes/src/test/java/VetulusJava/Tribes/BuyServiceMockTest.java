package VetulusJava.Tribes;

import VetulusJava.Tribes.Entities.Building;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Entities.Resource;
import VetulusJava.Tribes.Entities.Troop;
import VetulusJava.Tribes.Enum.BuildingType;
import VetulusJava.Tribes.Enum.ResourceType;
import VetulusJava.Tribes.Repositories.IBuildingRepository;
import VetulusJava.Tribes.Repositories.IResourceRepository;
import VetulusJava.Tribes.Services.BuildingService.IBuildingService;
import VetulusJava.Tribes.Services.BuyService.*;
import VetulusJava.Tribes.Services.TroopService.ITroopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class BuyServiceMockTest {

    @Autowired
    BuyService buyService;

    @Mock
    IResourceRepository resourceRepository;
    @Mock
    ITroopService troopService;
    @Mock
    IBuildingService buildingService;
    @Mock
    IBuildingRepository buildingRepository;

    Kingdom kingdom = new Kingdom();
    Kingdom kingdom2 = new Kingdom();
    Resource resource = new Resource(ResourceType.GOLD, 1, 1, kingdom);
    Resource resourceWithMoney = new Resource(ResourceType.GOLD, 100, 1, kingdom2);
    Building building = new Building(BuildingType.BARRACKS, 5, 15, 1000L, 1200L,kingdom2);
    BuyRequest request = new BuyRequest(kingdom2, BuyingType.TROOP);
    Troop troop = new Troop(1, 1200, kingdom2);
    BuyResponse buyResponse = new BuyResponse(troop,HttpStatus.OK);
    BuyResponse buyResponseResult = new BuyResponse(troop,HttpStatus.OK);



    @BeforeEach
    void setUp() {
        buyService = new BuyService(resourceRepository, troopService, buildingService, buildingRepository);
    }

    @Test
    @DisplayName("Buy troop without gold")
    public void whenExceptionThrown_NotEnoughGold() {
        when(resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom)).thenReturn(resource);
        BuyRequest request = new BuyRequest(kingdom, BuyingType.TROOP);
        BuyResponse actuaal = (BuyResponse) buyService.buy(request);
        assertThat(actuaal.getMessage()).isEqualTo("Not enough gold!");
    }

    @Test
    @DisplayName("Buy train without gold")
    public void trainTroop_NotEnoughGold() {
        when(resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom)).thenReturn(resource);
        BuyRequest request = new BuyRequest(kingdom,BuyingType.TRAINTROOP,2);
        BuyResponse actuaal = (BuyResponse) buyService.buy(request);
        assertThat(actuaal.getMessage()).isEqualTo("Not enough gold!");
    }

/*     @Test
     @DisplayName("Buy troop with gold")
     public void EnoughGold() {
         Resource resource = new Resource(ResourceType.GOLD, 100, 1, kingdom);
         when(resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom)).thenReturn(resource);
         BuyRequest request = new BuyRequest(kingdom,BuyingType.TROOP);
         BuyResponse actuaal = (BuyResponse) buyService.buy(request);
         Troop troop = (Troop) actuaal.getTroop();
         //assertThat(troop.getLevel()).isNotNull();
         assertThat(actuaal.getMessage()).isNull();
     }*/

  // @Test
  // @DisplayName("Buy train with gold")
  // public void BuyTroop() {
  //     when( resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom2)).thenReturn(resourceWithMoney);
  //     when(troopService.createTroop(request.getKingdom(), buyResponse)).thenReturn(buyResponse);
  //     BuyResponse actuaal = buyService.buy(request);
  //     assertThat(actuaal.getMessage()).isEmpty();
  //     assertThat(actuaal.getTroop()).isNotNull();
  // }

/*    @Test
    @DisplayName("Train troop with gold")
    public void EnoughGoldTrainTroop() {
        Resource resource = new Resource(ResourceType.GOLD, 100, 1, kingdom);
        when(resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom)).thenReturn(resource);
        BuyRequest request = new BuyRequest(kingdom,BuyingType.TRAINTROOP,2);
        BuyResponse actuaal = (BuyResponse) buyService.buy(request);
        Troop troop = (Troop) actuaal.getTroop();
        //assertThat(troop.getLevel()).isNotNull();
        assertThat(actuaal.getMessage()).isNull();
    }*/
}
