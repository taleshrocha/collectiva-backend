package br.ufrn.imd.collectiva_backend.repository;

import java.util.List;
import java.util.Optional;

import br.ufrn.imd.collectiva_backend.model.RefreshToken;
import br.ufrn.imd.collectiva_backend.model.UserInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends GenericRepository<RefreshToken> {

    boolean existsByUser(UserInfo userInfo);

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> getByUser(UserInfo user);


    @Transactional
    @Modifying
    @Query(value = "UPDATE refresh_token SET active = FALSE WHERE active = TRUE AND user_id = :userId", nativeQuery = true)
    void deleteAllByUser(Long userId);
}
