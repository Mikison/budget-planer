package pl.sonmiike.reportsservice.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserReportService {

    private final UserReportRepository userReportRepository;


    public Set<UserReport> fetchAllUsers() {
        return new HashSet<>(userReportRepository.findAll());
    }

    public Optional<UserReport> fetchUserById(Long userId) {
        return userReportRepository.findById(userId);
    }


}
