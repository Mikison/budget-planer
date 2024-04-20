package pl.sonmiike.reportsservice.user;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserEntityService {

    private final UserEntityRepository userEntityRepository;


    public Set<UserEntityReport> getAllUsers() {
        return new HashSet<>(userEntityRepository.findAll());
    }

    public UserEntityReport getUserById(Long userId) {
        return userEntityRepository.findById(userId).orElse(null);
    }

    public Long getUserId(Authentication authentication) {
        UserEntityReport user = (UserEntityReport) authentication.getPrincipal();
        return user.getUserId();
    }
}
