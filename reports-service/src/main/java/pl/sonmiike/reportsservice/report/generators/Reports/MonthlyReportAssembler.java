package pl.sonmiike.reportsservice.report.generators.Reports;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static pl.sonmiike.reportsservice.expense.ExpenseOperations.*;
import static pl.sonmiike.reportsservice.income.IncomeOperations.getTotalIncomes;

@Component
@RequiredArgsConstructor
public class MonthlyReportAssembler {


    private final UserEntityService userEntityService;
    private final IncomeEntityService incomeEntityService;
    private final ExpenseEntityService expenseEntityService;


    public Set<MonthlyReport> createMonthlyReport() {
        DateInterval date = getDateInterval();
        Set<MonthlyReport> monthlyReports = new HashSet<>();

        for (UserEntityReport user : userEntityService.getAllUsers()) {
            Optional<List<IncomeEntity>> incomeOpt = incomeEntityService.getIncomesFromDateInterval(date.getStartDate(), date.getEndDate(), user.getUserId());
            Optional<List<ExpenseEntity>> expensesOpt = expenseEntityService.getExpensesFromDateBetween(date.getStartDate(), date.getEndDate(), user.getUserId());

            if (incomeOpt.isEmpty() || incomeOpt.get().isEmpty() || expensesOpt.isEmpty() || expensesOpt.get().isEmpty()) {
                continue;
            }

            List<IncomeEntity> incomes = incomeOpt.get();
            List<ExpenseEntity> expenses = expensesOpt.get();

            MonthlyReport monthlyReport = buildMonthlyReport(user, date, incomes, expenses);
            monthlyReports.add(monthlyReport);
        }

        return monthlyReports;
    }

    private MonthlyReport buildMonthlyReport(UserEntityReport user, DateInterval dateInterval, List<IncomeEntity> incomes, List<ExpenseEntity> expenses) {
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
//                .totalProfitPercentage(calculateTotalProfitPercentage(incomes, expenses)) // Assuming implemented
                .budgetSummary(totalIncomes.subtract(totalExpenses))
                .expensesList(expenses)
                .incomeList(incomes)
//                .categoryExpenses(calculateCategoryExpenses(expenses)) // Assuming implemented
                .build();
    }


    private DateInterval getDateInterval() {
        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).minusDays(1);

        return new DateInterval(startDate, endDate);
    }
}
