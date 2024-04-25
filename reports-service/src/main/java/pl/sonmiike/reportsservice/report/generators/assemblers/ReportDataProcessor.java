package pl.sonmiike.reportsservice.report.generators.assemblers;

import pl.sonmiike.reportsservice.cateogry.Category;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.income.IncomeEntity;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@FunctionalInterface
public interface ReportDataProcessor<T extends Report> {
    T buildReport(UserEntityReport user, DateInterval date, List<IncomeEntity> incomes, List<ExpenseEntity> expenses, HashMap<Category, BigDecimal> categoryExpenses);

}
