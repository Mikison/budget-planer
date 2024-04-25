package pl.sonmiike.reportsservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.sonmiike.reportsservice.user.UserEntityReport;
import pl.sonmiike.reportsservice.user.UserEntityRepository;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserEntityRepository userEntityRepository;


    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Optional<UserEntityReport> userEntityReport = userEntityRepository.findByEmail(email);
            return userEntityReport.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with email :" + email));
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
