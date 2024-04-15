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
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static pl.sonmiike.reportsservice.expense.ExpenseOperations.*;
import static pl.sonmiike.reportsservice.income.IncomeOperations.getTotalIncomes;

@Component
@RequiredArgsConstructor
public class WeeklyReportAssembler {



    private final IncomeEntityService incomeEntityService;
    private final ExpenseEntityService expenseEntityService;
    private final CategoryEntityService categoryEntityService;



    public WeeklyReport createWeeklyReport(UserEntityReport user) {
        DateInterval date = getDateInterval();

        List<CategoryEntity> categories = categoryEntityService.getCategories();

            List<IncomeEntity> incomes = incomeEntityService.getIncomesFromDateInterval(date.getStartDate(), date.getEndDate(), user.getUserId())
                    .orElse(Collections.emptyList())
                    .stream()
                    .sorted(Comparator.comparing(IncomeEntity::getIncomeDate))
                    .toList();


            List<ExpenseEntity> expenses = expenseEntityService.getExpensesFromDateBetween(date.getStartDate(), date.getEndDate(), user.getUserId())
                    .orElse(Collections.emptyList())
                    .stream()
                    .sorted(Comparator.comparing(ExpenseEntity::getDate))
                    .toList();

            if (incomes.isEmpty() && expenses.isEmpty()) return null;
        Set<Long> categoryIds = expenses.stream().map(ExpenseEntity::getCategory).map(CategoryEntity::getId).collect(Collectors.toSet());
        List<CategoryEntity> userCategories = categories.stream()
                .filter(category -> categoryIds.contains(category.getId()))
                .collect(Collectors.toList());

        HashMap<CategoryEntity, BigDecimal> categoryExpenses = calculateCategoryExpenses(expenses, userCategories);
        return buildWeeklyReport(user, date, incomes, expenses, categoryExpenses);

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

    private WeeklyReport buildWeeklyReport(UserEntityReport user, DateInterval dateInterval, List<IncomeEntity> incomes, List<ExpenseEntity> expenses, HashMap<CategoryEntity, BigDecimal> categoryExpenses) {
        BigDecimal totalIncomes = getTotalIncomes(incomes);
        BigDecimal totalExpenses = calculateTotalExpenses(expenses);

        return WeeklyReport.builder()
                .user(user)
                .dateInterval(dateInterval)
                .totalExpenses(totalExpenses)
                .biggestExpense(findMaxExpense(expenses))
                .smallestExpense(findMinExpense(expenses))
                .averageDailyExpense(calculateAverageDailyExpenses(expenses, Period.between(dateInterval.getStartDate(), dateInterval.getEndDate()).getDays() + 1))
                .totalIncomes(totalIncomes)
                .budgetSummary(totalIncomes.subtract(totalExpenses))
                .expensesList(expenses)
                .incomeList(incomes)
                .categoryExpenses(categoryExpenses)
                .build();
    }

        private DateInterval getDateInterval() {
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
        LocalDate endDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        if (endDate.getMonth() != startDate.getMonth()) {
            startDate = endDate.withDayOfMonth(1);
        }

        return new DateInterval(startDate, endDate);
    }
}
