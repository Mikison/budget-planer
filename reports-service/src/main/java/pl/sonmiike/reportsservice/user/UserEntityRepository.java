package pl.sonmiike.reportsservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntityReport, Long> {
    Optional<UserEntityReport> findByEmail(String username);
}
