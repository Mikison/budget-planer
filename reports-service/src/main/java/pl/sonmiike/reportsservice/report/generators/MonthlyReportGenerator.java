package pl.sonmiike.reportsservice.report.generators;

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

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static pl.sonmiike.reportsservice.expense.ExpenseOperations.*;
import static pl.sonmiike.reportsservice.income.IncomeOperations.getTotalIncomes;

@Component
@RequiredArgsConstructor
public class MonthlyReportGenerator {


    private final UserEntityService userEntityService;
    private final IncomeEntityService incomeEntityService;
    private final ExpenseEntityService expenseEntityService;


    public Set<MonthlyReport> createMonthlyReport() {
        DateInterval date = getDateInterval();
        Set<MonthlyReport> monthlyReports = new HashSet<>();
        for (UserEntityReport user : userEntityService.getAllUsers()) {
            Optional<List<IncomeEntity>> income = incomeEntityService.getIncomesFromDateInterval(date.getStartDate(), date.getEndDate(), user.getUserId());
            Optional<List<ExpenseEntity>> expenses = expenseEntityService.getExpensesFromDateBetween(date.getStartDate(), date.getEndDate(), user.getUserId());
            if (income.isPresent() && expenses.isPresent()) {
                MonthlyReport monthlyReport = MonthlyReport.builder()
                        .user(user)
                        .dateInterval(date)
                        .totalExpenses(calculateTotalExpenses(expenses.get()))
                        .averageWeeklyExpense(calculateAverageWeeklyExpenses(expenses.get()))
                        .weekWithHighestExpenses(calculateWeekWithBiggestExpense(expenses.get()))
                        .dayWithHighestAverageExpense(getDayWithHighestAverageExpense(expenses.get()))
                        .totalIncomes(getTotalIncomes(income.get()))
//                        .totalProfitPercentage(calculateTotalProfitPercentage(income.get(), expenses.get())) // TODO implement
                        .budgetSummary(getTotalIncomes(income.get()).subtract(calculateTotalExpenses(expenses.get())))
                        .expensesList(expenses.get())
                        .incomeList(income.get())
//                        .categoryExpenses(calculateCategoryExpenses(expenses.get())) // Implement
                        .build();

                monthlyReports.add(monthlyReport);
            }
        }
        return monthlyReports;

    }


    private DateInterval getDateInterval() {
        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).minusDays(1);

        return new DateInterval(startDate, endDate);
    }
}
