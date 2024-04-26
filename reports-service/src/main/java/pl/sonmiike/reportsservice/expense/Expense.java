package pl.sonmiike.reportsservice.expense;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sonmiike.reportsservice.cateogry.Category;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDate date;
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserReport user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    public Expense(BigDecimal amount) {
        this.amount = amount;
    }

    public Expense(BigDecimal amount, LocalDate date) {
        this.date = date;
        this.amount = amount;
    }
}
