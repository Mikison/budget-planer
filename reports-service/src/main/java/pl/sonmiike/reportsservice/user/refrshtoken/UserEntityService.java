package pl.sonmiike.reportsservice.user.refrshtoken;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.user.UserEntityReport;
import pl.sonmiike.reportsservice.user.UserEntityRepository;

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
}
