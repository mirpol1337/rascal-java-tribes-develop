/*
package VetulusJava.Tribes.KingdomTests;

import VetulusJava.Tribes.DTOs.BuildingIdDTO;
import VetulusJava.Tribes.Services.BuildingService.IBuildingService;
import VetulusJava.Tribes.Services.KingdomService.IKingdomService;
import VetulusJava.Tribes.Services.ResourceService.IResourceService;
import VetulusJava.Tribes.Services.TroopService.TroopService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class KingdomTroopsPostTest {

    @MockBean
    TroopService troopService;
    @Autowired
    IResourceService resourceService;
    @Autowired
    IKingdomService kingdomService;
    @Autowired
    IBuildingService buildingService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

// logic changed. Integration test for the endpoint will follow after implementing buying service
    @Test
    @WithMockUser
    public void troopPostTest() throws Exception {

        BuildingIdDTO testDTO = new BuildingIdDTO();
        testDTO.buildingId = 2;

        mockMvc.perform(post("/kingdom/troops")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finishedAt").value(12399999));
    }
}
*/
