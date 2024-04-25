package pl.sonmiike.authenticationservice.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "This field can't be empty")
    private String name;

    @NotBlank(message = "This field can't be empty")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "This field can't be empty")
    @Column(unique = true)
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "This field can't be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;


    @Enumerated(EnumType.STRING)
    private RoleEnum role;

}