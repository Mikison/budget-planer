package pl.sonmiike.reportsservice.report.generators.assemblers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.cateogry.CategoryEntity;
import pl.sonmiike.reportsservice.cateogry.CategoryEntityService;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.expense.ExpenseEntityService;
import pl.sonmiike.reportsservice.income.IncomeEntity;
import pl.sonmiike.reportsservice.income.IncomeEntityService;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.MonthlyReport;
import pl.sonmiike.reportsservice.user.UserEntityReport;
import pl.sonmiike.reportsservice.user.UserEntityService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static pl.sonmiike.reportsservice.expense.ExpenseOperations.*;
import static pl.sonmiike.reportsservice.income.IncomeOperations.getTotalIncomes;

@Component
@RequiredArgsConstructor
public class MonthlyReportAssembler {


    private final UserEntityService userEntityService;
    private final IncomeEntityService incomeEntityService;
    private final ExpenseEntityService expenseEntityService;
    private final CategoryEntityService categoryEntityService;


    public Set<MonthlyReport> createMonthlyReport() {
        DateInterval date = getDateInterval();
        Set<MonthlyReport> monthlyReports = new HashSet<>();
        List<CategoryEntity> categories = categoryEntityService.getCategories();

        userEntityService.getAllUsers().forEach(user -> {
            List<IncomeEntity> incomes = incomeEntityService.getIncomesFromDateInterval(date.getStartDate(), date.getEndDate(), user.getUserId())
                    .orElse(Collections.emptyList())
                    .stream()
                    .sorted(Comparator.comparing(IncomeEntity::getIncomeDate))
                    .collect(Collectors.toList());

            List<ExpenseEntity> expenses = expenseEntityService.getExpensesFromDateBetween(date.getStartDate(), date.getEndDate(), user.getUserId())
                    .orElse(Collections.emptyList())
                    .stream()
                    .sorted(Comparator.comparing(ExpenseEntity::getDate))
                    .collect(Collectors.toList());



            if (!incomes.isEmpty() && !expenses.isEmpty()) {
                Set<Long> categoryIds = expenses.stream().map(ExpenseEntity::getCategory).map(CategoryEntity::getId).collect(Collectors.toSet());
                List<CategoryEntity> userCategories = categories.stream()
                        .filter(category -> categoryIds.contains(category.getId()))
                        .toList();

                HashMap<CategoryEntity, BigDecimal> categoryExpenses = calculateCategoryExpenses(expenses, userCategories);
                MonthlyReport monthlyReport = buildMonthlyReport(user, date, incomes, expenses, categoryExpenses);
                monthlyReports.add(monthlyReport);
            }
        });

        return monthlyReports;
    }

    private HashMap<CategoryEntity, BigDecimal> calculateCategoryExpenses(List<ExpenseEntity> expenses, List<CategoryEntity> userCategories) {
        HashMap<CategoryEntity, BigDecimal> categoryExpenses = new HashMap<>();
        for (CategoryEntity category : userCategories) {
            BigDecimal categoryExpense = calculateTotalExpenses(expenses.stream()
                    .filter(expense -> expense.getCategory().getId().equals(category.getId()))
                    .toList());
            categoryExpenses.put(category, categoryExpense);
        }
        return categoryExpenses;
    }

    private MonthlyReport buildMonthlyReport(UserEntityReport user, DateInterval dateInterval, List<IncomeEntity> incomes, List<ExpenseEntity> expenses, HashMap<CategoryEntity, BigDecimal> categoryExpenses) {
        BigDecimal totalIncomes = getTotalIncomes(incomes);
        BigDecimal totalExpenses = calculateTotalExpenses(expenses);

        return MonthlyReport.builder()
                .user(user)
                .dateInterval(dateInterval)
                .totalExpenses(totalExpenses)
                .averageWeeklyExpense(calculateAverageWeeklyExpenses(expenses))
                .weekWithHighestExpenses(calculateWeekWithBiggestExpense(expenses))
                .dayWithHighestAverageExpense(getDayWithHighestAverageExpense(expenses))
                .totalIncomes(totalIncomes)
                .budgetSummary(totalIncomes.subtract(totalExpenses))
                .expensesList(expenses)
                .incomeList(incomes)
                .categoryExpenses(categoryExpenses)
                .build();
    }


    private DateInterval getDateInterval() {
        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).minusDays(1);

        return new DateInterval(startDate, endDate);
    }
}
