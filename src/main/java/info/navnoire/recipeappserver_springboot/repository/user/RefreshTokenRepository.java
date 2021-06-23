package info.navnoire.recipeappserver_springboot.repository.user;

import info.navnoire.recipeappserver_springboot.domain.user.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long userId);

    Optional<RefreshToken> findByToken(String token);

    int deleteByUserId(Long userId);
}
