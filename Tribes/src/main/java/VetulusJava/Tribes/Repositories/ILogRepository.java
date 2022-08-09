package VetulusJava.Tribes.Repositories;

import VetulusJava.Tribes.Entities.Log;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

    public interface ILogRepository extends CrudRepository<Log, Long> {
    }
