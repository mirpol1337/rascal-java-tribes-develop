
package VetulusJava.Tribes.Repositories;

import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Entities.Resource;
import VetulusJava.Tribes.Enum.ResourceType;
import org.apache.commons.codec.language.RefinedSoundex;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IResourceRepository extends CrudRepository<Resource, Long> {
    Resource findByType(ResourceType type);

    Resource findByTypeAndKingdom(ResourceType type, Kingdom kingdom);

    Iterable<Resource> findByTypeAndAmountGreaterThan(ResourceType type, Integer number);
}
