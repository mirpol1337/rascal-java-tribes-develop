//todo
package VetulusJava.Tribes.Services;

import VetulusJava.Tribes.Entities.ApplicationUser;
import VetulusJava.Tribes.Entities.Building;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Enum.BuildingType;
import VetulusJava.Tribes.Repositories.IBuildingRepository;
import VetulusJava.Tribes.Repositories.IUserRepository;
import VetulusJava.Tribes.Services.BuildingService.BuildingService;
import VetulusJava.Tribes.Services.BuildingService.IBuildingService;
import VetulusJava.Tribes.Services.BuyService.BuyResponse;
import VetulusJava.Tribes.Services.UserService.IUserService;
import VetulusJava.Tribes.Services.UserService.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class BuildingServiceTest {
    private IBuildingService buildingService;
    private IBuildingRepository buildingRepository = mock(IBuildingRepository.class);
    private static Building testBuilding = new Building(BuildingType.FARM, 1, 1, System.currentTimeMillis(), System.currentTimeMillis());

    @BeforeEach
    void setBuildingService() {
        buildingService = new BuildingService(buildingRepository);
    }

    //@Test
    //public void createByType() {
    //    when(buildingService.createByType(testBuilding.type));
    //    ApplicationUser actual = buildingService.getById()
    //}

    @Test
    @DisplayName("find building by type and kingdom id")
        //function called only internally. Arguments provided by other services which should guarantee that the arguments passed are valid
    void getBuildingByTypeAndKingdomId() {
        when(buildingRepository.findByTypeAndKingdomId(BuildingType.FARM, 1L)).thenReturn(Optional.ofNullable(testBuilding));
        Building actual = buildingService.getByTypeAndKingdomId(BuildingType.FARM, 1L);
        assertEquals(testBuilding, actual);
    }
}
