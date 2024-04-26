package pl.sonmiike.reportsservice.report.generators.assemblers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.cateogry.Category;
import pl.sonmiike.reportsservice.cateogry.CategoryService;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.expense.ExpenseService;
import pl.sonmiike.reportsservice.expense.ExpenseOperations;
import pl.sonmiike.reportsservice.income.Income;
import pl.sonmiike.reportsservice.income.IncomeService;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public abstract class BaseReportAssembler {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    protected abstract DateInterval getDateInterval();

    protected List<Income> fetchSortedIncomes(DateInterval date, Long userId) {
        return incomeService.getIncomesFromDateInterval(date.getStartDate(), date.getEndDate(), userId)
                .orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(Income::getIncomeDate))
                .toList();
    }

    protected List<Expense> fetchSortedExpenses(DateInterval date, Long userId) {
        return expenseService.getExpensesFromDateBetween(date.getStartDate(), date.getEndDate(), userId)
                .orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(Expense::getDate))
                .toList();
    }

    protected HashMap<Category, BigDecimal> calculateCategoryExpenses(List<Expense> expenses, List<Category> categories) {
        Set<Long> categoryIds = expenses.stream()
                .map(Expense::getCategory)
                .map(Category::getId)
                .collect(Collectors.toSet());
        List<Category> userCategories = categories.stream()
                .filter(category -> categoryIds.contains(category.getId()))
                .toList();

        HashMap<Category, BigDecimal> categoryExpenses = new HashMap<>();
        for (Category category : userCategories) {
            BigDecimal categoryExpense = ExpenseOperations.calculateTotalExpenses(expenses.stream()
                    .filter(expense -> expense.getCategory().getId().equals(category.getId()))
                    .collect(Collectors.toList()));
            categoryExpenses.put(category, categoryExpense);
        }
        return categoryExpenses;
    }

    protected <T extends Report> T createReport(UserReport user, ReportDataProcessor<T> processor) {
        DateInterval date = getDateInterval();
        List<Category> categories = categoryService.getCategories();
        List<Income> incomes = fetchSortedIncomes(date, user.getUserId());
        List<Expense> expenses = fetchSortedExpenses(date, user.getUserId());

        if (incomes.isEmpty() && expenses.isEmpty()) return null;

        HashMap<Category, BigDecimal> categoryExpenses = calculateCategoryExpenses(expenses, categories);

        return processor.buildReport(user, date, incomes, expenses, categoryExpenses);
    }
}
