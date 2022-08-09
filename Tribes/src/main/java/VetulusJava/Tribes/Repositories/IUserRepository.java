package VetulusJava.Tribes.Repositories;

import VetulusJava.Tribes.Entities.ApplicationUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserRepository extends CrudRepository<ApplicationUser, UUID> {
    ApplicationUser findByName(String name);
    ApplicationUser findByEmail(String email);
    ApplicationUser findByNameAndAndEmail(String name, String email);
}