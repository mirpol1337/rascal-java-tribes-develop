package VetulusJava.Tribes.Services.TimeService;

import VetulusJava.Tribes.Entities.Time;
import VetulusJava.Tribes.Repositories.ITimeRepository;
import VetulusJava.Tribes.Services.ArmyService.IArmyService;
import VetulusJava.Tribes.Services.ResourceService.IResourceService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class TimeService {
    ITimeRepository timeRepository;
    private IResourceService resourceService;
    private static long serverStart;
    private long timeNow;
    IArmyService armyService;

    public TimeService(IResourceService resourceService, ITimeRepository timeRepository, IArmyService armyService) {
        this.resourceService = resourceService;
        this.timeRepository = timeRepository;
        this.armyService = armyService;
    }

    @Scheduled(cron = "*/60 * * * * *")
    public void ResourceGeneration() {
        checkTime();
        resourceService.calculateProduction();
    }

    public void checkTime() {
        Time timeTable=timeRepository.findFirstByOrderByIdAsc();
        long ticks = timeTable.getTicks()+1;
        serverStart = timeTable.getServerStart(); //1594123200 - 07.07.2020 14:00:00
        this.timeNow = System.currentTimeMillis() / 1000;
        long expectedTicks = (timeNow - serverStart) / 60;
        long ticksDifference = expectedTicks - ticks;
        if (expectedTicks != ticks) {
            for (int i = 0; i < ticksDifference; i++) {
                ticks++;
                resourceService.calculateProduction();
                System.out.println("bezim");
            }
        }
        timeTable.setTicks(ticks);
        timeRepository.save(timeTable);
    }

    @Scheduled(cron = "*/60 * * * * *")
    public void checkBattles(){
        armyService.fight();
    }

    @Scheduled(cron = "*/60 * * * * *")
    public void checkReturningArmies(){
        armyService.disbandArmy();
    }
}