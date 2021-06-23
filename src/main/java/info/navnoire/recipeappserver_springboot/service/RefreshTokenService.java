package info.navnoire.recipeappserver_springboot.service;

import info.navnoire.recipeappserver_springboot.domain.user.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId);

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserId(Long userId);

    Optional<RefreshToken> verifyTokenExpiration(RefreshToken token);

    int deleteByUserId(Long userId);

}
