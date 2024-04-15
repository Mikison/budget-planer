package pl.sonmiike.reportsservice.user.refreshtoken;

import jakarta.persistence.*;
import lombok.*;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.time.Instant;

@Entity
@Table(name = "refresh_token")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @Column(nullable = false)
    private Instant expirationTime;

    @OneToOne
    private UserEntityReport user;
}
