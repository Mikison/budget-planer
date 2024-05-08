package pl.sonmiike.reportsservice.report.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sonmiike.reportsservice.category.Category;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.income.Income;
import pl.sonmiike.reportsservice.report.repository.ReportType;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReport implements Report {

    private UserReport user;
    private DateInterval dateInterval;
    private BigDecimal totalExpenses;
    private Expense largestExpense;
    private BigDecimal averageWeeklyExpense;
    private DateInterval weekWithHighestExpenses;
    private DayOfWeek dayWithHighestAverageExpense;
    private BigDecimal totalIncomes;
    private BigDecimal budgetSummary;
    private List<Expense> expensesList;
    private List<Income> incomeList;
    private HashMap<Category, BigDecimal> categoryExpenses;


    @Override
    public Map<ReportDataKey, Object> getReportData() {
        Map<ReportDataKey, Object> dataMap = new HashMap<>();
        dataMap.put(ReportDataKey.USER, user.getUsername());
        dataMap.put(ReportDataKey.DATE_INTERVAL, dateIntervalToString(dateInterval));
        dataMap.put(ReportDataKey.TOTAL_EXPENSES, totalExpenses.toPlainString());
        dataMap.put(ReportDataKey.LARGEST_EXPENSE, largestExpense);
        dataMap.put(ReportDataKey.AVERAGE_WEEKLY_EXPENSE, averageWeeklyExpense.toPlainString());
        dataMap.put(ReportDataKey.WEEK_WITH_HIGHEST_EXPENSES, dateIntervalToString(weekWithHighestExpenses));
        dataMap.put(ReportDataKey.DAY_WITH_HIGHEST_AVERAGE_EXPENSE, dayWithHighestAverageExpense);
        dataMap.put(ReportDataKey.TOTAL_INCOMES, totalIncomes.toPlainString());
        dataMap.put(ReportDataKey.BUDGET_SUMMARY, budgetSummary.toPlainString());
        dataMap.put(ReportDataKey.EXPENSES_LIST, expensesList);
        dataMap.put(ReportDataKey.INCOME_LIST, incomeList);
        dataMap.put(ReportDataKey.CATEGORY_EXPENSES, categoryExpenses);

        return dataMap;


    }

    @Override
    public ReportType getReportType() {
        return ReportType.MONTHLY_REPORT;
    }

    private String dateIntervalToString(DateInterval dateInterval) {
        return dateInterval.getStartDate() + "-" + dateInterval.getEndDate();
    }
}
