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
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("User", user.getUsername());
        dataMap.put("Date Interval", dateIntervalToString(dateInterval));
        dataMap.put("Total Expenses", totalExpenses.toPlainString());
        dataMap.put("Largest Expense", largestExpense);
        dataMap.put("Average Weekly Expense", averageWeeklyExpense.toPlainString());
        dataMap.put("Week With Highest Expenses", dateIntervalToString(weekWithHighestExpenses));
        dataMap.put("Day With Highest Average Expense", dayWithHighestAverageExpense);
        dataMap.put("Total Incomes", totalIncomes.toPlainString());
//        dataMap.put("Total Profit Percentage", totalProfitPercentage.toPlainString());
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
        return dateInterval.getStartDate() + " - " + dateInterval.getEndDate();
    }
}
