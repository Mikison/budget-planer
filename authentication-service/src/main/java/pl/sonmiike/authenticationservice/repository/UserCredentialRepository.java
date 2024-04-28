package pl.sonmiike.authenticationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sonmiike.authenticationservice.entity.UserCredential;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

    Optional<UserCredential> findByEmail(String email);

    boolean existsByEmail(String email);

}
