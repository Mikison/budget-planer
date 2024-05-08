package pl.sonmiike.reportsservice.report.assemblers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.category.Category;
import pl.sonmiike.reportsservice.category.CategoryCalculator;
import pl.sonmiike.reportsservice.category.CategoryService;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.expense.ExpenseFetcher;
import pl.sonmiike.reportsservice.income.Income;
import pl.sonmiike.reportsservice.income.IncomeFetcher;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.report.util.ReportDataProcessor;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public abstract class BaseReportAssembler {

    protected final IncomeFetcher incomeFetcher;
    protected final ExpenseFetcher expenseFetcher;
    protected final CategoryCalculator categoryCalculator;
    protected final CategoryService categoryService;

    protected abstract DateInterval getDateInterval();


    protected <T extends Report> T createReport(UserReport user, ReportDataProcessor<T> processor) {
        DateInterval date = getDateInterval();
        List<Category> categories = categoryService.getCategories();
        List<Income> incomes = incomeFetcher.fetchSortedIncomes(date, user.getUserId());
        List<Expense> expenses = expenseFetcher.fetchSortedExpenses(date, user.getUserId());

        if (incomes.isEmpty() && expenses.isEmpty()) return null;

        HashMap<Category, BigDecimal> categoryExpenses =categoryCalculator.calculateCategoryExpenses(expenses, categories);

        return processor.buildReport(user, date, incomes, expenses, categoryExpenses);
    }
}
