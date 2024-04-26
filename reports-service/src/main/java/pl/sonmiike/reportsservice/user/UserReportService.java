package pl.sonmiike.reportsservice.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserReportService {

    private final UserReportRepository userReportRepository;


    public Set<UserReport> getAllUsers() {
        return new HashSet<>(userReportRepository.findAll());
    }

    public UserReport getUserById(Long userId) {
        return userReportRepository.findById(userId).orElse(null);
    }


}
