package pl.sonmiike.reportsservice.report.util;

import pl.sonmiike.reportsservice.category.Category;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.income.Income;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@FunctionalInterface
public interface ReportDataProcessor<T extends Report> {
    T buildReport(UserReport user, DateInterval date, List<Income> incomes, List<Expense> expenses, HashMap<Category, BigDecimal> categoryExpenses);
}
