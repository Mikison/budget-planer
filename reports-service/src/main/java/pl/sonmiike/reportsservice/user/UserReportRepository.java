package pl.sonmiike.reportsservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    Optional<UserReport> findByEmail(String username);
}
