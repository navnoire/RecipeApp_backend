package info.navnoire.recipeappserver_springboot.repository.user;

import info.navnoire.recipeappserver_springboot.domain.user.Role;
import info.navnoire.recipeappserver_springboot.domain.user.Role.ERole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
