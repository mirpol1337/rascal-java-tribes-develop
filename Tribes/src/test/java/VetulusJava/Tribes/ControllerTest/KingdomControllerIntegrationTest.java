package VetulusJava.Tribes.ControllerTest;

import VetulusJava.Tribes.DTOs.BuildingLevelDTO;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Enum.BuildingType;
import VetulusJava.Tribes.Enum.ResourceType;
import VetulusJava.Tribes.Repositories.IKingdomRepository;
import VetulusJava.Tribes.Repositories.IResourceRepository;
import VetulusJava.Tribes.Services.BuildingService.BuildingService;
import VetulusJava.Tribes.Services.SeedingService.InitializeDB;
import VetulusJava.Tribes.Controllers.KingdomController;
import VetulusJava.Tribes.Security.CurrentUser;
import VetulusJava.Tribes.Services.BuyService.IBuyService;
import VetulusJava.Tribes.Services.KingdomService.IKingdomService;
import VetulusJava.Tribes.Services.TroopService.ITroopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KingdomControllerIntegrationTest {

    BuildingLevelDTO buildingLevelDTO = new BuildingLevelDTO(11);
    BuildingLevelDTO buildingLevelDTO1 = new BuildingLevelDTO(8);
    BuildingLevelDTO buildingLevelDTO3 = new BuildingLevelDTO(13);
    BuildingLevelDTO buildingLevelDTO2 = new BuildingLevelDTO(10);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    KingdomController kingdomController;
    @Autowired
    IKingdomService kingdomService;
    @Autowired
    ITroopService troopService;
    @Autowired
    IBuyService buyService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    public CurrentUser currentUser;
    @Autowired
    private InitializeDB initializeDB;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private IResourceRepository resourceRepository;
    @Autowired
    private IKingdomRepository kingdomRepository;
    private Kingdom testKingdom;

    @BeforeAll
    private void init(){
        initializeDB.seedDB();
        this.testKingdom = kingdomRepository.findById(1L).get();
    }

    @Test
    @DisplayName("get all troops for the user")
    void getAllTroopsForTheCurrentUser() throws Exception {
        mockMvc.perform(get("/kingdom/troops")   //token with name Bond and validity till 2030
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.troops").isArray());
    }

    @Test
    @DisplayName("get all troops , without authorization")
    void getAllTroopsNoTokenProvided() throws Exception {
        mockMvc.perform(get("/kingdom/troops")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("get kingdom by id - success")
    public void getKingdomByIdTestReturnsKingdom() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/{id}", 1)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TestKingdom"));
    }

    @Test
    @DisplayName("get kingdom by id - id not found")
    public void getKingdomByIdTestReturnsNotFoundException() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/{id}", 9999)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("find resource by type - success")
    public void findResourcebyType() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/resources/{type}", "GOLD")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(ResourceType.GOLD.toString()));
    }

    @Test
    @DisplayName("find resource by type - bad request")
    public void findResourcebyTypeNotExist() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/resources/{type}", "WOOD")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("buy troop - good request")
    public void buyTroops() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/kingdom/troops")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("update troop - bad request, id lower then 1")
    public void updateTroopsBadLevel() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/troops/{level}","0" )
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("update troop - bad request, id is char")
    public void updateTroopsBadLevelChar() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/troops/{level}","l" )
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("update troop - bad request, without parametr")
    public void updateTroopsBadLevelWithoutParametr() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/troops")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("update troop - bad request, not found id")
    public void updateTroopsBadLevelNotFoundId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/troops/{level}","1000")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("update troop - ok request,  found id")
    public void updateTroopsBadLevelFoundId() throws Exception {
        initializeDB.seedDB();
        this.mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/troops/{level}","2")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("update building - bad request,  bad id")
    public void updateBuildingBadId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/buildings/{buildingId}","0")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("update building - bad request,  good id, missing BuildingDto")
    public void updateBuildingGoodId() throws Exception {
        var buildingId = buildingService.getByTypeAndKingdomId(BuildingType.TOWNHALL,testKingdom.getId()).getID();
        this.mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/buildings/{buildingId}",buildingId)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

  @Test
  @DisplayName("upgrade building success")
  public void upgradeBuildingSuccess() throws  Exception{
      var goldResource = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, testKingdom);
      goldResource.setAmount(1600);
      resourceRepository.save(goldResource);
      var buildingId = buildingService.getByTypeAndKingdomId(BuildingType.TOWNHALL,testKingdom.getId()).getID();
      this.mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/buildings/{buildingId}", buildingId)
              .contentType("application/json")
              .content(objectMapper.writeValueAsString(buildingLevelDTO))
              .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
              .accept(MediaType.APPLICATION_JSON))
              .andDo(print())
              .andExpect(status().isOk())
              .andExpect(MockMvcResultMatchers.jsonPath("$.level").value(11));
  }

    @Test
    @DisplayName("upgrade building bad request level bigger more then one as level of building")
    public void upgradeBuildingBadRequestMoreThenOneLevel() throws  Exception{
        var goldResource = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, testKingdom);
        goldResource.setAmount(1600);
        resourceRepository.save(goldResource);
        var buildingId = buildingService.getByTypeAndKingdomId(BuildingType.TOWNHALL,testKingdom.getId()).getID();
        this.mockMvc.perform(MockMvcRequestBuilders.put("/kingdom/buildings/{buildingId}", buildingId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(buildingLevelDTO3))
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("You cannot upgrade your building more than +1 level"));
    }

    @Test
    @DisplayName("get building for kingdom")
    public void getKingdom() throws Exception {
        initializeDB.seedDB();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/kingdom")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCb25kIiwiZXhwIjoxOTA3NjE4NDUzLCJpYXQiOjE5MDcyNTg0NTN9.s7uSn9VQDT06bjuEfmleyhMqqA3tlAVdPURqw9S1Uq4")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
