package pl.sonmiike.reportsservice.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntityReport, Long> {
}
