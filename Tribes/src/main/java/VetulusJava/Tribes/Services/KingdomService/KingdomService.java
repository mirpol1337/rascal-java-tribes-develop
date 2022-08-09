package VetulusJava.Tribes.Services.KingdomService;

import VetulusJava.Tribes.DTOs.KingdomCredentialsDTO;
import VetulusJava.Tribes.Entities.Constants;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Repositories.IKingdomRepository;
import VetulusJava.Tribes.Services.BuildingService.IBuildingService;
import VetulusJava.Tribes.Services.ResourceService.IResourceService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Qualifier("KingdomService")
public class KingdomService implements IKingdomService {
    private IKingdomRepository repository;
    private IResourceService resourceService;
    private IBuildingService buildingService;

    public KingdomService(IKingdomRepository repository, IResourceService resourceService, IBuildingService buildingService) {
        this.repository = repository;
        this.resourceService = resourceService;
        this.buildingService = buildingService;
    }

    public Kingdom getById(long id) {
        var kingdom = repository.findById(id);
        return kingdom.isPresent() ? kingdom.get() : null;
    }

    public Kingdom getByUserId(UUID userId) {
        return repository.findKingdomByUser_Id(userId);
    }

    @Override
    public Kingdom saveKingdom(Kingdom kingdom) {
        return repository.save(kingdom);
    }

    @Override
    public void setFullKingdom(Kingdom kingdom) {
        resourceService.setStartingResources(kingdom);
        buildingService.setStartingBuildings(kingdom);

    }

    @Override
    public Kingdom usersKingdom(UUID uuid) {
        return repository.findKingdomByUser_Id(uuid);
    }

    @Override
    public Kingdom setKingdomLocation(Kingdom kingdom, int spreadRange) {
        if(!repository.getKigdomByLocation(0,0).isPresent()){
            kingdom.setX(0);
            kingdom.setY(0);
        }else {
            Kingdom lastKingdom = repository.getLastCreatedKingdom().get();

            int lastX = lastKingdom.getX();
            int lastY = lastKingdom.getY();

            int newX;
            int newY;

            // Placing next Kingdom to an adjacent quadrant of the mam on a mirror location
            // If the last kingdom is in the 1st or 3rd quadrant, flip X location
            // Otherwise flip X location
            if((lastX>0 && lastY>0)||(lastX<0 && lastY<0)){
                newX = - lastX;
                newY = lastY;
            }else {
                newX = lastX;
                newY = - lastY;
            }
            var rng = new Random();

            // Adding random spread of <-spreadRange,spreadRange> in each direction around the mirrored location
            newX += rng.nextInt(2*spreadRange + 1) - spreadRange;
            newY += rng.nextInt(2*spreadRange + 1) - spreadRange;

            //Checking map boarders, if it is out of bounds, set it to the max
            if (newX < -50){
                newX = -50;
            }
            if (newX > 50){
                newX = 50;
            }
            if(newY < -50){
                newY = -50;
            }
            if(newY > 50){
                newY = 50;
            }

            kingdom.setX(newX);
            kingdom.setY(newY);

            // Check if there is any kingdom on the generated location
            // If the location is occupied, rerun the generation with increased spread
            if(repository.getKigdomByLocation(newX, newY).isPresent()){
                kingdom = setKingdomLocation(kingdom, ++spreadRange);
            }
        }
            return kingdom;
    }

    @Override
    public double calculateDistance(Kingdom kingdom1, Kingdom kingdom2){
        int distX = Math.abs(kingdom1.getX() - kingdom2.getX());
        int distY = Math.abs(kingdom1.getY() - kingdom2.getY());
        return Math.sqrt(distX*distX + distY*distY);
    }
    @Override
    public int calculateTravelTime(double distance){
        return (int)distance * Constants.TRAVEL_TIME_PER_DISTANCE;
    }

    @Override
    public Collection<KingdomCredentialsDTO> getKingdoms() {
        Collection<Kingdom> k = repository.findAll();
        Collection<KingdomCredentialsDTO> result = new ArrayList<>();
        for (var kingdom:k) {
            result.add(new KingdomCredentialsDTO(kingdom.getId(),kingdom.getName(),kingdom.getX(),kingdom.getY()));
        }
        return  result;
    }
}