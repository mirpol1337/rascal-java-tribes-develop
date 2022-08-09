package VetulusJava.Tribes.Repositories;

import VetulusJava.Tribes.Entities.Time;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITimeRepository extends CrudRepository<Time,Long> {
    Time findFirstByOrderByIdAsc();
}
