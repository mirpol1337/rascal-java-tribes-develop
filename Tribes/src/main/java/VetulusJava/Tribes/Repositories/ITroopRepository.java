package VetulusJava.Tribes.Repositories;

import VetulusJava.Tribes.Entities.Troop;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ITroopRepository extends CrudRepository<Troop, Long> {

    @Query("SELECT t FROM Troop t WHERE t.kingdom.id = :kingdomId")
    Collection<Troop> findAllByKingdomId(@Param("kingdomId") Long kingdomId);

    @Query(value = "SELECT * FROM Troop WHERE finished_at > :currentTime AND kingdom_id = :kingdomId", nativeQuery = true)
    Collection<Troop> findTroopsByFinishedAtGreaterThanAndKingdomId(@Param("kingdomId") Long kingdomId, @Param("currentTime") long currentTime);

    @Query(value = "Select * FROM Troop WHERE kingdom_id = :kingdomId AND level = :level LIMIT 1", nativeQuery = true)
    Optional<Troop> findFirstByLevelAndKingdomId(@Param("level") int level, @Param("kingdomId") Long kingdomId);

    @Query(value = "SELECT * FROM Troop WHERE kingdom_id = :kingdomId ORDER BY level ASC LIMIT :count", nativeQuery = true)
    Collection<Troop> findTopByLevelAndKingdomId(@Param("count") int count, @Param("kingdomId") Long kingdomId);

    @Transactional
    @Modifying
    @Override
    void deleteAll(Iterable<? extends Troop> entities);

    @Query("SELECT t FROM Troop t WHERE t.kingdom.id = :kingdomId AND t.level = :level AND t.isInArmy= :isInArmy AND t.finishedAt <= :currentTime")
    Set<Troop> findAllByLevelAndKingdomIdAndInArmyAndFinishedAtBefore(@Param("level") int level, @Param("kingdomId") Long kingdomId, @Param("isInArmy") boolean isInArmy, @Param("currentTime") long currentTime);

    @Query("SELECT t FROM Troop t WHERE t.kingdom.id = :kingdomId AND t.isInArmy = :isInArmy AND t.finishedAt <= :currentTime")
    Set<Troop> findAllByKingdomIdAndInArmyAndFinishedAtBefore(@Param("kingdomId") Long kingdomId, @Param("isInArmy") boolean isInArmy, @Param("currentTime") long currentTime);
}