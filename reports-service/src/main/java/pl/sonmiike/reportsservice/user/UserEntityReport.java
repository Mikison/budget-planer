package pl.sonmiike.reportsservice.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.sonmiike.reportsservice.user.refreshtoken.RefreshTokenEntity;

import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "users")
@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntityReport implements UserDetails {

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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
