package pl.sonmiike.reportsservice.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.sonmiike.reportsservice.user.refreshtoken.RefreshTokenEntity;


@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntityReport {

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

    @OneToOne
    private RefreshTokenEntity refreshToken;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;


    public UserEntityReport(Long userId) {
        this.userId = userId;
    }
}
