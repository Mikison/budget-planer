package pl.sonmiike.reportsservice.report.generators.assemblers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.cateogry.CategoryEntity;
import pl.sonmiike.reportsservice.cateogry.CategoryEntityService;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.expense.ExpenseEntityService;
import pl.sonmiike.reportsservice.expense.ExpenseOperations;
import pl.sonmiike.reportsservice.income.IncomeEntity;
import pl.sonmiike.reportsservice.income.IncomeEntityService;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public abstract class BaseReportAssembler {

    private final IncomeEntityService incomeEntityService;
    private final ExpenseEntityService expenseEntityService;
    private final CategoryEntityService categoryEntityService;

    protected abstract DateInterval getDateInterval();

    protected List<IncomeEntity> fetchSortedIncomes(DateInterval date, Long userId) {
        return incomeEntityService.getIncomesFromDateInterval(date.getStartDate(), date.getEndDate(), userId)
                .orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(IncomeEntity::getIncomeDate))
                .toList();
    }

    protected List<ExpenseEntity> fetchSortedExpenses(DateInterval date, Long userId) {
        return expenseEntityService.getExpensesFromDateBetween(date.getStartDate(), date.getEndDate(), userId)
                .orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(ExpenseEntity::getDate))
                .toList();
    }

    protected HashMap<CategoryEntity, BigDecimal> calculateCategoryExpenses(List<ExpenseEntity> expenses, List<CategoryEntity> categories) {
        Set<Long> categoryIds = expenses.stream()
                .map(ExpenseEntity::getCategory)
                .map(CategoryEntity::getId)
                .collect(Collectors.toSet());
        List<CategoryEntity> userCategories = categories.stream()
                .filter(category -> categoryIds.contains(category.getId()))
                .toList();

        HashMap<CategoryEntity, BigDecimal> categoryExpenses = new HashMap<>();
        for (CategoryEntity category : userCategories) {
            BigDecimal categoryExpense = ExpenseOperations.calculateTotalExpenses(expenses.stream()
                    .filter(expense -> expense.getCategory().getId().equals(category.getId()))
                    .collect(Collectors.toList()));
            categoryExpenses.put(category, categoryExpense);
        }
        return categoryExpenses;
    }

    protected <T extends Report> T createReport(UserEntityReport user, ReportDataProcessor<T> processor) {
        DateInterval date = getDateInterval();
        List<CategoryEntity> categories = categoryEntityService.getCategories();
        List<IncomeEntity> incomes = fetchSortedIncomes(date, user.getUserId());
        List<ExpenseEntity> expenses = fetchSortedExpenses(date, user.getUserId());

        if (incomes.isEmpty() && expenses.isEmpty()) return null;

        HashMap<CategoryEntity, BigDecimal> categoryExpenses = calculateCategoryExpenses(expenses, categories);

        return processor.buildReport(user, date, incomes, expenses, categoryExpenses);
    }
}
