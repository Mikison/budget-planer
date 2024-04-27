package pl.sonmiike.reportsservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.user.UserReport;
import pl.sonmiike.reportsservice.user.UserReportRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceClass implements UserDetailsService {

    private final UserReportRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserReport> userEntityReport = repository.findByEmail(username);
        return userEntityReport.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with email :" + username));
    }
}
