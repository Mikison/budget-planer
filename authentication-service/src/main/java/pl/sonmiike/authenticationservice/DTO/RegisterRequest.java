package pl.sonmiike.authenticationservice.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import pl.sonmiike.authenticationservice.validators.email.ValidEmail;
import pl.sonmiike.authenticationservice.validators.password.ValidPassword;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, message = "Name should have at least 3 characters")
    private String name;

    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, message = "Username should have at least 3 characters")
    private String username;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @ValidEmail
    private String email;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @ValidPassword
    private String password;
}
