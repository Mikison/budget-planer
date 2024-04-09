package pl.sonmiike.reportsservice.report;

import lombok.*;
import pl.sonmiike.financewebapi.expenses.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeeklyReport extends Report {
    private Expense biggestExpense;
    private Expense smallestExpense;
    private BigDecimal percentageOfBugdetSpent; // if exists
}
