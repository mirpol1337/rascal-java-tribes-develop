package VetulusJava.Tribes.Repositories;

import VetulusJava.Tribes.Entities.Building;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Enum.BuildingType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface IBuildingRepository extends CrudRepository<Building, Long> {
    @Query("SELECT b FROM Building b WHERE b.type = :type AND b.kingdom.id = :kingdomId")
    Optional<Building> findByTypeAndKingdomId(@Param ("type") BuildingType type, @Param("kingdomId") Long kingdomId);

    Building findByKingdomAndType(Kingdom kingdom, BuildingType buildingType);

    Building findByIDAndAndKingdom(long id, Kingdom kingdom);

    @Query("SELECT b FROM Building b WHERE b.finished_at > :currentTime AND b.kingdom = :kingdom")
    Collection<Building> findBuildingsByFinishedAtGreaterThanAndKingdom(@Param("kingdom") Kingdom kingdom, @Param("currentTime") long currentTime);

    Collection<Building> findBuildingByKingdom(Kingdom kingdom);
}
