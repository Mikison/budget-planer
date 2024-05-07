package pl.sonmiike.reportsservice.report.types;


import lombok.Builder;
import lombok.Getter;
import pl.sonmiike.reportsservice.category.Category;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.income.Income;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Getter
public class WeeklyReport implements Report {

    private UserReport user;
    private DateInterval dateInterval;
    private BigDecimal totalExpenses;
    private Expense biggestExpense;
    private Expense smallestExpense;
    private BigDecimal averageDailyExpense;
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
        dataMap.put("Biggest Expense", biggestExpense);
        dataMap.put("Smallest Expense", smallestExpense);
        dataMap.put("Average Daily Expense", averageDailyExpense.toPlainString());
        dataMap.put("Total Incomes", totalIncomes.toPlainString());
        dataMap.put("Budget Summary", budgetSummary.toPlainString());
        dataMap.put("Expenses List", expensesList);
        dataMap.put("Income List", incomeList);
        dataMap.put("Category Expenses", categoryExpenses);

        return dataMap;

    }

    @Override
    public ReportType getReportType() {
        return ReportType.WEEKLY_REPORT;
    }


    private String dateIntervalToString(DateInterval dateInterval) {
        return dateInterval.getStartDate() + "-" + dateInterval.getEndDate();
    }
}
