package VetulusJava.Tribes.Repositories;

import VetulusJava.Tribes.Entities.Army;
import VetulusJava.Tribes.Entities.Troop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;


@Repository
public interface IArmyRepository extends JpaRepository<Army, Long> {
    Set<Army> findAllByKingdomId(Long kingdomId);

    @Query("SELECT a FROM Army a WHERE a.arrivedAt <= :currentTime AND a.goingToBattle= true")
    Set<Army> findAllByArrivedAtBeforeAndGoingToBattle(@Param("currentTime") long currentTime);

    @Query("SELECT a FROM Army a WHERE a.returnedAt <= :currentTime")
    Set<Army> findAllByReturnedAtBefore(@Param("currentTime") long currentTime);

    @Transactional
    @Modifying
    @Query("DELETE FROM Army a WHERE a.id = :armyId")
    void delete(@Param("armyId") long armyId);

    @Transactional
    @Modifying
    Army save(Army army);
}
