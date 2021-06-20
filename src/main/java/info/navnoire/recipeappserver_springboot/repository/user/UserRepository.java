package info.navnoire.recipeappserver_springboot.repository.user;

import info.navnoire.recipeappserver_springboot.domain.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Victoria Berezina on 17/06/2021 in RecipeApp project
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
