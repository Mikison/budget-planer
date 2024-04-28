package pl.sonmiike.authenticationservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {


    private String name;
    private String username;
    private String email;
    private String password;
}
