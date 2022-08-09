package VetulusJava.Tribes.Repositories;

import VetulusJava.Tribes.Entities.ApplicationUser;
import VetulusJava.Tribes.Entities.Kingdom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IKingdomRepository extends CrudRepository<Kingdom, Long> {
    Kingdom findKingdomByUser(ApplicationUser user);
    Kingdom findKingdomByUser_Id(UUID uuid);
    Kingdom findKingdomByName(String name);

    @Query(value = "SELECT * FROM Kingdom WHERE id = (SELECT MAX(id) FROM Kingdom) LIMIT 1", nativeQuery = true)
    Optional<Kingdom> getLastCreatedKingdom();

    @Query(value ="SELECT * FROM Kingdom WHERE x = :x AND y = :y LIMIT 1", nativeQuery = true)
    Optional<Kingdom> getKigdomByLocation(@Param("x") int x, @Param("y") int y);

    Collection<Kingdom> findAll();
}
