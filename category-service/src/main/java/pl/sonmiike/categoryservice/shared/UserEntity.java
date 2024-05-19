package pl.sonmiike.categoryservice.shared;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

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
    private UserRole role;





}
