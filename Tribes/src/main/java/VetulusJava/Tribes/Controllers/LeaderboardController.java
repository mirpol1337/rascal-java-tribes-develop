package VetulusJava.Tribes.Controllers;

import VetulusJava.Tribes.DTOs.LeaderboardDTO;
import VetulusJava.Tribes.Services.LeaderboardService.ILeaderboardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController extends BaseController {

    private ILeaderboardService leaderboardService;

    public LeaderboardController(ILeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @ApiOperation(value = "show leaderboard by building",response = LeaderboardDTO.class, authorizations = {@Authorization(value = "jwtToken")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok")})
    @GetMapping("/buildings")
    public ResponseEntity<LeaderboardDTO> BuildingsLeaderboard(){
        LeaderboardDTO leaderboard = new LeaderboardDTO(leaderboardService.getBuildingLeaderboard());
        return new ResponseEntity<LeaderboardDTO>(leaderboard, HttpStatus.OK);
    }

    @ApiOperation(value = "show leaderboard by troops", response = LeaderboardDTO.class,authorizations = {@Authorization(value = "jwtToken")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok")})
    @GetMapping("/troops")
    public ResponseEntity<LeaderboardDTO> TroopsLeaderboard(){
        LeaderboardDTO leaderboard = new LeaderboardDTO(leaderboardService.getTroopsLeaderboard());
        return new ResponseEntity<LeaderboardDTO>(leaderboard, HttpStatus.OK);
    }

}
