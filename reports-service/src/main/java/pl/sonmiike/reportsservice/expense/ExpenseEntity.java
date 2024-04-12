package pl.sonmiike.reportsservice.expense;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sonmiike.reportsservice.cateogry.CategoryEntity;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "expense")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDate date;
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntityReport user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;


    public String detailedString() {

        return "Expense" +
                ", Name: '" + name + '\'' +
                ", Description: '" + description + '\'' +
                ", Date: " + date +
                ", Amount: " + amount.toPlainString() +
                ", Category: " + category.getName();
    }
}
