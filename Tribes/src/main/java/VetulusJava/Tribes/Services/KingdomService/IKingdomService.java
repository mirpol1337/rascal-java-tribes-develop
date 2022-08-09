package VetulusJava.Tribes.Services.KingdomService;

import VetulusJava.Tribes.DTOs.KingdomCredentialsDTO;
import VetulusJava.Tribes.Entities.Kingdom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface IKingdomService {
    Kingdom getById(long id);
    Kingdom getByUserId(UUID userId);
    Kingdom saveKingdom(Kingdom kingdom);
    void setFullKingdom(Kingdom kingdom);
    Kingdom usersKingdom(UUID uuid);
    Kingdom setKingdomLocation(Kingdom kingdom, int spreadRange);
    double calculateDistance(Kingdom kingdom1, Kingdom kingdom2);
    public int calculateTravelTime(double distance);

    Collection<KingdomCredentialsDTO> getKingdoms();
}
