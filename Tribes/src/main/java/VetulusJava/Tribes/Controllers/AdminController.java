package VetulusJava.Tribes.Controllers;

import VetulusJava.Tribes.Services.SeedingService.InitializeDB;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("local")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private InitializeDB initializeDB;

    public AdminController(InitializeDB initializeDB) {
        this.initializeDB = initializeDB;
    }

    @ApiOperation(value = "")
    @GetMapping("/seed")
    public String SeedDb() {
        initializeDB.seedDB();
        return "seeding";
    }

    @ApiOperation(value = "")
    @GetMapping("/clean")
    public String CleanDb() {
        initializeDB.cleanDB();
        return "cleaning";
    }
}
