package pl.sonmiike.reportsservice.report.types;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.math.BigDecimal;
import java.time.DayOfWeek;

@Builder
@Data
public class MonthlyReport implements Report {

    private UserEntityReport user;
    private DateInterval dateInterval;
    private BigDecimal totalExpenses;
    private ExpenseEntity largestExpense;
    private BigDecimal averageWeeklyExpense;
    private DateInterval weekWithHighestExpenses;
    private DayOfWeek dayWithHighestAverageExpense;
    private BigDecimal totalIncomes;
    private BigDecimal totalProfitPercentage;
    private BigDecimal budgetSummary;


}
