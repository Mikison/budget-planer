package pl.sonmiike.reportsservice.report;

import lombok.*;
import pl.sonmiike.financewebapi.expenses.Expense;

import java.math.BigDecimal;
import java.time.DayOfWeek;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthlyReport extends Report {

    private DateInterval weekWithBiggestExpense;
    private DayOfWeek dayWithHighestAverageExpense;
    private BigDecimal averageWeeklyExpenses;


}
