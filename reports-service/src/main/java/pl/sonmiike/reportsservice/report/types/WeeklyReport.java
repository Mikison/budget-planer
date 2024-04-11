package pl.sonmiike.reportsservice.report.types;


import lombok.Builder;
import lombok.Data;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.math.BigDecimal;

@Data
@Builder
public class WeeklyReport implements Report {

    private UserEntityReport user;
    private DateInterval dateInterval;
    private BigDecimal totalExpenses;
    private ExpenseEntity biggestExpense;
    private ExpenseEntity smallestExpense;
    private BigDecimal averageDailyExpense;
    private BigDecimal totalIncomes;
    private BigDecimal percentageOfBudgetSpent;
    private BigDecimal budgetSummary;
}
