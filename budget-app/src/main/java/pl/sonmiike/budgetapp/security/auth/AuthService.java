package pl.sonmiike.budgetapp.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.sonmiike.budgetapp.user.UserEntity;

@Service
@RequiredArgsConstructor
public class AuthService {

    public Long getUserId(Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        return user.getUserId();
    }
}