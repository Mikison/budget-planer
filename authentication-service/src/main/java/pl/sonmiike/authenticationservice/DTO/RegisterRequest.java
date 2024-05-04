package pl.sonmiike.authenticationservice.DTO;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.sonmiike.authenticationservice.validators.email.ValidEmail;
import pl.sonmiike.authenticationservice.validators.password.ValidPassword;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NotNull
    private String name;
    @NotNull
    private String username;
    @NotNull
    @ValidEmail
    private String email;
    @NotNull
    @ValidPassword
    private String password;
}
