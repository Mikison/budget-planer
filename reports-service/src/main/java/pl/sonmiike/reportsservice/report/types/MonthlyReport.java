package pl.sonmiike.reportsservice.report.types;

import lombok.Builder;
import lombok.Data;
import pl.sonmiike.reportsservice.cateogry.CategoryEntity;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.income.IncomeEntity;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private List<ExpenseEntity> expensesList;
    private List<IncomeEntity> incomeList;
    private HashMap<CategoryEntity, BigDecimal> categoryExpenses;


    @Override
    public Map<String, Object> getReportData() {
        return Map.of();
    }

    @Override
    public ReportType getReportType() {
        return ReportType.MONTHLY_REPORT;
    }
}
