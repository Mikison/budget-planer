package pl.sonmiike.reportsservice.user.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RefreshTokenEntityRepository extends JpaRepository<RefreshTokenEntity, Long> {
}
