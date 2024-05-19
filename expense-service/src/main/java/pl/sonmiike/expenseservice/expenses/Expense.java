package pl.sonmiike.expenseservice.expenses;


import jakarta.persistence.*;
import lombok.*;
import pl.sonmiike.expenseservice.shared.Category;
import pl.sonmiike.expenseservice.shared.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDate date;
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    void update(ExpenseDTO updateData) {
        if (updateData.getName() != null) this.name = updateData.getName();

        if (updateData.getDescription() != null) this.description = updateData.getDescription();

        if (updateData.getDate() != null) this.date = updateData.getDate();

        if (updateData.getAmount() != null) this.amount = updateData.getAmount();
    }
}
