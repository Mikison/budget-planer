package pl.sonmiike.reportsservice.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserEntityService {

    private final UserEntityRepository userEntityRepository;


    public Set<UserEntityReport> getAllUsers() {
        System.out.println(userEntityRepository.findAll());
        return new HashSet<>(userEntityRepository.findAll());
    }

    public UserEntityReport getUserById(Long userId) {
        return userEntityRepository.findById(userId).orElse(null);
    }
}
