package pl.sonmiike.reportsservice.report.types;


import lombok.Builder;
import lombok.Data;
import pl.sonmiike.reportsservice.cateogry.CategoryEntity;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.income.IncomeEntity;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<ExpenseEntity> expensesList;
    private List<IncomeEntity> incomeList;
    private HashMap<CategoryEntity, BigDecimal> categoryExpenses;

    @Override
    public Map<String, String> getReportData() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("User", user.getName());
        dataMap.put("Date Interval", dateInterval.toString());
        dataMap.put("Total Expenses", totalExpenses.toPlainString());
        dataMap.put("Biggest Expense", detailedString(biggestExpense));
        dataMap.put("Smallest Expense", detailedString(smallestExpense));
        dataMap.put("Average Daily Expense", averageDailyExpense.toPlainString());
        dataMap.put("Total Incomes", totalIncomes.toPlainString());
//        dataMap.put("Percentage of Budget Spent", percentageOfBudgetSpent.toPlainString());
        dataMap.put("Budget Summary", budgetSummary.toPlainString());
        dataMap.put("Expenses List", listToStringCoverter(expensesList));
        dataMap.put("Income List", listToStringCoverter(incomeList));
//        dataMap.put("Category Expenses", categoryExpenses.toString());


        return dataMap;

    }

    private String listToStringCoverter(List<?> list) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : list) {
            sb.append(obj.toString()).append("\n");
        }
        return sb.toString();
    }

//    private String convertMapToString(HashMap<CategoryEntity, BigDecimal> categoryExpenses) {
//        StringBuilder result = new StringBuilder();
//        for (Map.Entry<CategoryEntity, BigDecimal> entry categoryExpenses.entrySet()) {
//            CategoryEntity category = entry.getKey();
//            BigDecimal expense = entry.getValue();
//            result.append(category.getName()).append("").append(expense).append("\n");
//        }
//        return result.toString().trim();
//    }

    private String detailedString(ExpenseEntity expense) {
        StringBuilder sb = new StringBuilder();
        sb.append("Expense");
        sb.append(", Name'").append(expense.getName()).append('\'');
        sb.append(", Description'").append(expense.getDescription()).append('\'');
        sb.append(", Date").append(expense.getDate());
        sb.append(", Amount").append(expense.getAmount().toPlainString());
        sb.append(", Category").append(expense.getCategory().getName());
        return sb.toString();
    }
}
