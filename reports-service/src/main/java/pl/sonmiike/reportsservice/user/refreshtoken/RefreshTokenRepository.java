package pl.sonmiike.reportsservice.user.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
}
