package pl.sonmiike.financewebapi.category;

import jakarta.persistence.*;
import lombok.*;
import pl.sonmiike.financewebapi.user.UserEntity;


import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    private String iconUrl;

    private LocalDateTime assignedAt;

}
