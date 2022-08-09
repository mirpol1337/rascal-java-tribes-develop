package VetulusJava.Tribes.Controllers;

import VetulusJava.Tribes.DTOs.*;
import VetulusJava.Tribes.Entities.*;
import VetulusJava.Tribes.Enum.ResourceType;
import VetulusJava.Tribes.Exceptions.BadRequestException;
import VetulusJava.Tribes.Exceptions.NotFoundException;
import VetulusJava.Tribes.Services.ArmyService.IArmyService;
import VetulusJava.Tribes.Services.BuildingService.IBuildingService;
import VetulusJava.Tribes.Services.BuyService.BuyRequest;
import VetulusJava.Tribes.Services.BuyService.BuyResponse;
import VetulusJava.Tribes.Services.BuyService.BuyingType;
import VetulusJava.Tribes.Services.BuyService.IBuyService;
import VetulusJava.Tribes.Services.KingdomService.IKingdomService;
import VetulusJava.Tribes.Services.ResourceService.IResourceService;
import VetulusJava.Tribes.Services.TroopService.ITroopService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;

@RestController
@RequestMapping("/kingdom")
@Validated
public class KingdomController extends BaseController {
    private IResourceService resourceService;
    private IBuyService buyService;
    private IKingdomService kingdomService;
    private ITroopService troopService;
    private IBuildingService buildingService;
    private IArmyService armyService;

    public KingdomController(IBuyService buyService, IResourceService resourceService, IKingdomService kingdomService, ITroopService troopService, IBuildingService buildingservice, IArmyService armyService) {
        this.resourceService = resourceService;
        this.kingdomService = kingdomService;
        this.troopService = troopService;
        this.buildingService = buildingservice;
        this.buyService = buyService;
        this.armyService = armyService;
    }

    @ApiOperation(value = "show all kingdoms", response = Resource.class, authorizations = {@Authorization(value = "jwtToken")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok"), @ApiResponse(code = 403, message = "Access Denied")})
    @GetMapping("/kingdoms")
    public ResponseEntity<Collection<KingdomCredentialsDTO>> getKingdoms() {
        Collection<KingdomCredentialsDTO> result = kingdomService.getKingdoms();
        return new ResponseEntity<Collection<KingdomCredentialsDTO>>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "show all resources for user", response = Resource.class, authorizations = {@Authorization(value = "jwtToken")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok"), @ApiResponse(code = 403, message = "Access Denied")})
    @GetMapping()
    public ResponseEntity<KingdomDTO> showResourceDb() {
        Kingdom kingdom = kingdomService.getByUserId(getUserId());
        KingdomDTO result = new KingdomDTO();
        result.setName(kingdom.getName());
        result.setId(kingdom.getId());
        result.setBuildings(kingdom.getBuildings());
        result.setResources(kingdom.getResources());
        result.setX(kingdom.getX());
        result.setY(kingdom.getY());
        return new ResponseEntity<KingdomDTO>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "find resourses by type", authorizations = {@Authorization(value = "jwtToken")})
    @GetMapping("resources/{type}")
    public ResourceResponseDTO findResourceByType(@PathVariable ResourceType type) {
        var result = resourceService.findByTypeAndKingdom(type, kingdomService.getByUserId(getUserId()));
        if (result != null) {
            ResourceResponseDTO resourceResponseDTO = new ResourceResponseDTO(result.getType(), result.getAmount(), result.getGeneration());
            return resourceResponseDTO;
        } else {
            throw new NotFoundException("Type " + type + " was not found.");
        }
    }

    @ApiOperation(value = "get kingdom by id", response = Kingdom.class, authorizations = {@Authorization(value = "jwtToken")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 404, message = "Kingdom with ID was not found."),
            @ApiResponse(code = 400, message = "Invalid id")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Kingdom> getKingdom(@PathVariable @Min(value = 1, message = "Invalid id") long id) {
        if (kingdomService.getById(id) != null) {
            return new ResponseEntity<Kingdom>(kingdomService.getById(id), HttpStatus.OK);
        } else {
            throw new NotFoundException("Kingdom with ID " + id + " was not found.");
        }
    }

    @ApiOperation(value = "Get all buildings", authorizations = {@Authorization(value = "jwtToken")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok")})
    @GetMapping("/buildings")
    public ResponseEntity Buildings() {
        var result = buildingService.findByKingdom(kingdomService.getByUserId(getUserId()));
        if (result != null) {
            return new ResponseEntity(result, HttpStatus.valueOf(200));
        } else {
            throw new NotFoundException("Building were not found.");
        }
    }

    @ApiOperation(value = "", response = Building.class, authorizations = {@Authorization(value = "jwtToken")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok"),
            @ApiResponse(code = 404, message = "building id not found"),
            @ApiResponse(code = 400, message = "Invalid building level!"),
            @ApiResponse(code = 400, message = "Maximum level is 20"),
            @ApiResponse(code = 400, message = "Missing parameter(s): type!"),
            @ApiResponse(code = 400, message = "Invalid building id")
    })
    @PutMapping("/buildings/{buildingId}")
    public ResponseEntity<Building> Building(@PathVariable @Min(value = 1, message = "Invalid building id") long buildingId, @Valid @RequestBody BuildingLevelDTO level) {
        BuyResponse response = (BuyResponse) buyService.buy(new BuyRequest(kingdomService.getByUserId(getUserId()), BuyingType.UPGRADEBUILDING, level.getLevel(), buildingId));
        checkForException(response);
        return new ResponseEntity<Building>((Building) response.getBuilding(), HttpStatus.OK);
    }

    @ApiOperation(value = "get all troops", response = TroopsDTO.class, authorizations = {@Authorization(value = "jwtToken")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok")})
    @GetMapping("/troops")
    public ResponseEntity<TroopsDTO> GetTroops() {
        TroopsDTO troops = new TroopsDTO();
        troops.setTroops(troopService.getTroops(kingdomService.getByUserId(getUserId()).getId()));
        return new ResponseEntity<TroopsDTO>(troops, HttpStatus.OK);
    }

    @ApiOperation(value = "buy new troops", response = Troop.class, authorizations = {@Authorization(value = "jwtToken")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok")})
    @PostMapping("/troops")
    public ResponseEntity<Troop> CreateTroop() {
        BuyResponse response = (BuyResponse) buyService.buy(new BuyRequest(kingdomService.getByUserId(getUserId()), BuyingType.TROOP));
        checkForException(response);
        return new ResponseEntity<Troop>((Troop) response.getTroop(), HttpStatus.OK);
    }

    @ApiOperation(value = "troop for train tropp", response = BadRequestException.class, authorizations = {@Authorization(value = "jwtToken")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok")})
    @PutMapping("/troops/{level:[0-9]+}")
    public ResponseEntity<Troop> UpdateTroop(@PathVariable("level") @Min(value = 1, message = "Invalid troop level!") Integer level) {
        BuyResponse response = (BuyResponse) buyService.buy(new BuyRequest(kingdomService.getByUserId(getUserId()), BuyingType.TRAINTROOP, level));
        checkForException(response);
        return new ResponseEntity<Troop>((Troop) response.getTroop(), HttpStatus.OK);
    }

    //handle empty parameter in method
    @ApiOperation(value = "enpoind without path variable", response = BadRequestException.class, authorizations = {@Authorization(value = "jwtToken")})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Missing parameter(s): type!")})
    @PutMapping("/troops")
    public void Troop() {
        throw new BadRequestException("Missing parameter(s): type!");
    }

    @ApiOperation(value = "when user use string in path variable", response = NotFoundException.class, authorizations = {@Authorization(value = "jwtToken")})
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Invalid troop level!")})
    @PutMapping("/troops/{lvl:[A-Za-z]+}")
    public void TroopCharOrString(@PathVariable("lvl") String lvl) {
        throw new NotFoundException("Invalid troop level!");
    }

    @ApiOperation(value = "create and send an army", response = Army.class, authorizations = {@Authorization(value = "jwtToken")})
    //@ApiResponses(value = {@ApiResponse(code = 200, message = "ok")})
    @PostMapping("/army")
    public ResponseEntity<Army> SendArmy(@RequestBody SendArmyDTO sendArmyDTO) {
        if (sendArmyDTO.getTargetKingdomId() == null || kingdomService.getById(sendArmyDTO.getTargetKingdomId()) == null) {
            throw new NotFoundException("Target kingdom not found!");
        }
        return new ResponseEntity<Army>(armyService.createAndSendArmy(kingdomService.getByUserId(getUserId()), sendArmyDTO.getLevel(), kingdomService.getById(sendArmyDTO.getTargetKingdomId())), HttpStatus.OK);
    }

    @ApiOperation(value = "get armies for current user's kingdom", response = Army.class, authorizations = {@Authorization(value = "jwtToken")})
    //@ApiResponses(value = {@ApiResponse(code = 200, message = "ok")})
    @GetMapping("/army")
    public ResponseEntity<Collection<Army>> getArmies() {
        return new ResponseEntity<Collection<Army>>(armyService.getArmies(kingdomService.getByUserId(getUserId())), HttpStatus.OK);
    }
}
