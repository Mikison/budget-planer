package pl.sonmiike.reportsservice.report.generators;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.expense.ExpenseEntityService;
import pl.sonmiike.reportsservice.income.IncomeEntity;
import pl.sonmiike.reportsservice.income.IncomeEntityService;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.MonthlyReport;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserEntityReport;
import pl.sonmiike.reportsservice.user.UserEntityService;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static pl.sonmiike.reportsservice.expense.ExpenseOperations.*;
import static pl.sonmiike.reportsservice.income.IncomeOperations.getTotalIncomes;

@Component
@RequiredArgsConstructor
public class WeeklyReportGenerator {




    private final UserEntityService userEntityService;
    private final IncomeEntityService incomeEntityService;
    private final ExpenseEntityService expenseEntityService;


    public Set<WeeklyReport> createWeeklyReport() {
        DateInterval date = getDateInterval();
        Set<WeeklyReport> weeklyReports = new HashSet<>();
        for (UserEntityReport user : userEntityService.getAllUsers()) {
            Optional<List<IncomeEntity>> incomeOpt = incomeEntityService.getIncomesFromDateInterval(date.getStartDate(), date.getEndDate(), user.getUserId());
            Optional<List<ExpenseEntity>> expensesOpt = expenseEntityService.getExpensesFromDateBetween(date.getStartDate(), date.getEndDate(), user.getUserId());
            if (incomeOpt.isEmpty() || incomeOpt.get().isEmpty() || expensesOpt.isEmpty() || expensesOpt.get().isEmpty()) {
                continue;
            }
            List<IncomeEntity> incomes = incomeOpt.get();
            List<ExpenseEntity> expenses = expensesOpt.get();

            WeeklyReport monthlyReport = buildWeeklyReport(user, date, incomes, expenses);
            weeklyReports.add(monthlyReport);
        }
        return weeklyReports;

    }

    private WeeklyReport buildWeeklyReport(UserEntityReport user, DateInterval dateInterval, List<IncomeEntity> incomes, List<ExpenseEntity> expenses) {
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
//                .percentageOfBudgetSpent()
                .budgetSummary(totalIncomes.subtract(totalExpenses))
                .expensesList(expenses)
                .incomeList(incomes)
//                .categoryExpenses(calculateCategoryExpenses(expenses))
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
