package VetulusJava.Tribes.Services;

import VetulusJava.Tribes.Leaderboard.BuildingLeaderboardEntry;
import VetulusJava.Tribes.Leaderboard.LeaderboardEntry;
import VetulusJava.Tribes.Leaderboard.TroopLeaderboardEntry;
import VetulusJava.Tribes.Entities.Building;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Entities.Troop;
import VetulusJava.Tribes.Enum.BuildingType;
import VetulusJava.Tribes.Repositories.IBuildingRepository;
import VetulusJava.Tribes.Repositories.ITroopRepository;
import VetulusJava.Tribes.Services.KingdomService.IKingdomService;
import VetulusJava.Tribes.Services.LeaderboardService.ILeaderboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class LeaderboardServiceTest {

    @MockBean
    IBuildingRepository buildingRepository;
    @MockBean
    ITroopRepository troopRepository;
    @MockBean
    IKingdomService kingdomService;
    @Autowired
    ILeaderboardService leaderboardService;

    @Test
    public void getBuildingLeaderboardTest(){

        ArrayList<Building>mockBuildings = new ArrayList<>();
        Building building1 = new Building(BuildingType.TOWNHALL,20,10,12345L,23456L);
        Building building2 = new Building(BuildingType.TOWNHALL,15,10,12345L,23456L);
        Building building3 = new Building(BuildingType.BARRACKS,10,10,12345L,23456L);
        Kingdom kingdom1 = new Kingdom("Pepa", 1, 1);
        Kingdom kingdom2 = new Kingdom("Franta", 2, 2);
        kingdom1.setId(1L);
        kingdom2.setId(2L);
        building1.setKingdom(kingdom1);
        building2.setKingdom(kingdom2);
        building3.setKingdom(kingdom2);
        mockBuildings.add(building1);
        mockBuildings.add(building2);
        mockBuildings.add(building3);

        when(buildingRepository.findAll()).thenReturn(mockBuildings);
        when(kingdomService.getById(1)).thenReturn(kingdom1);
        when(kingdomService.getById(2)).thenReturn(kingdom2);
        ArrayList<LeaderboardEntry> actual = leaderboardService.getBuildingLeaderboard();

        var expected = new ArrayList<BuildingLeaderboardEntry>();
        expected.add(new BuildingLeaderboardEntry("Franta", 25));
        expected.add(new BuildingLeaderboardEntry("Pepa", 20));

        var actualEntry0 = (BuildingLeaderboardEntry)actual.get(0);
        var actualEntry1 = (BuildingLeaderboardEntry)actual.get(1);

        assertEquals(expected.get(0).kingdomName, actualEntry0.kingdomName);
        assertEquals(expected.get(1).kingdomName, actualEntry1.kingdomName);
        assertEquals(expected.get(0).buildings, actualEntry0.buildings);
        assertEquals(expected.get(1).buildings, actualEntry1.buildings);
    }

    @Test
    public void getTroopsLeaderboardTest(){
        ArrayList<Troop>mockTroops = new ArrayList<>();
        Troop troop1 = new Troop();
        Troop troop2 = new Troop();
        Troop troop3 = new Troop();
        troop1.setLevel(15);
        troop2.setLevel(10);
        troop3.setLevel(10);
        Kingdom kingdom1 = new Kingdom("Karel", 1, 1);
        Kingdom kingdom2 = new Kingdom("Vojta", 2, 2);
        kingdom1.setId(1L);
        kingdom2.setId(2L);
        troop1.setKingdom(kingdom1);
        troop2.setKingdom(kingdom2);
        troop3.setKingdom(kingdom2);
        mockTroops.add(troop1);
        mockTroops.add(troop2);
        mockTroops.add(troop3);

        when(troopRepository.findAll()).thenReturn(mockTroops);
        when(kingdomService.getById(1)).thenReturn(kingdom1);
        when(kingdomService.getById(2)).thenReturn(kingdom2);
        ArrayList<LeaderboardEntry>act = leaderboardService.getTroopsLeaderboard();

        var actualEntry0 = (TroopLeaderboardEntry)act.get(0);
        var actualEntry1 = (TroopLeaderboardEntry)act.get(1);

        var expected = new ArrayList<TroopLeaderboardEntry>();
        expected.add(new TroopLeaderboardEntry("Vojta", 20));
        expected.add(new TroopLeaderboardEntry("Karel", 15));

        assertEquals(expected.get(0).kingdomName, actualEntry0.kingdomName);
        assertEquals(expected.get(1).kingdomName, actualEntry1.kingdomName);
        assertEquals(expected.get(0).soldiers, actualEntry0.soldiers);
        assertEquals(expected.get(1).soldiers, actualEntry1.soldiers);
    }
}
