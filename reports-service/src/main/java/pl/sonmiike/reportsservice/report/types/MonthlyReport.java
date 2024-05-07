package pl.sonmiike.reportsservice.report.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.sonmiike.reportsservice.category.Category;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.income.Income;
import pl.sonmiike.reportsservice.report.database.ReportType;
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
    public Map<String, Object> getReportData() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("User", user.getUsername());
        dataMap.put("Date Interval", dateIntervalToString(dateInterval));
        dataMap.put("Total Expenses", totalExpenses.toPlainString());
        dataMap.put("Largest Expense", largestExpense);
        dataMap.put("Average Weekly Expense", averageWeeklyExpense.toPlainString());
        dataMap.put("Week With Highest Expenses", dateIntervalToString(weekWithHighestExpenses));
        dataMap.put("Day With Highest Average Expense", dayWithHighestAverageExpense);
        dataMap.put("Total Incomes", totalIncomes.toPlainString());
        dataMap.put("Budget Summary", budgetSummary.toPlainString());
        dataMap.put("Expenses List", expensesList);
        dataMap.put("Income List", incomeList);
        dataMap.put("Category Expenses", categoryExpenses);

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
