package pl.sonmiike.financewebapi.security.auth;

import lombok.Data;

@Data
public class RefreshTokenRequest {

    private String refreshToken;
}
