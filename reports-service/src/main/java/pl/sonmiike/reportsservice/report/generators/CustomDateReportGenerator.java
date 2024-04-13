package pl.sonmiike.reportsservice.report.generators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.expense.ExpenseEntityService;
import pl.sonmiike.reportsservice.income.IncomeEntity;
import pl.sonmiike.reportsservice.income.IncomeEntityService;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserEntityService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static pl.sonmiike.reportsservice.expense.ExpenseOperations.*;
import static pl.sonmiike.reportsservice.income.IncomeOperations.getTotalIncomes;


@Component
@RequiredArgsConstructor
public class CustomDateReportGenerator {


    private final UserEntityService userEntityService;
    private final IncomeEntityService incomeEntityService;
    private final ExpenseEntityService expenseEntityService;


    public WeeklyReport createCustomDateIntervalReport(Long userId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        Optional<List<IncomeEntity>> income = incomeEntityService.getIncomesFromDateInterval(startDate, endDate, userId);
        Optional<List<ExpenseEntity>> expenses = expenseEntityService.getExpensesFromDateBetween(startDate, endDate, userId);
        if (income.isPresent() && expenses.isPresent()) {
            return WeeklyReport.builder()
                    .user(userEntityService.getUserById(userId))
                    .dateInterval(new DateInterval(startDate, endDate))
                    .totalExpenses(calculateTotalExpenses(expenses.get()))
                    .biggestExpense(findMaxExpense(expenses.get()))
                    .smallestExpense(findMinExpense(expenses.get()))
                    .averageDailyExpense(calculateAverageDailyExpenses(expenses.get(), Period.between(startDate, endDate).getDays() + 1))
                    .totalIncomes(getTotalIncomes(income.get()))
                    // TODO Percentage of Budget Spent to be implemented
                    .budgetSummary(getTotalIncomes(income.get()).subtract(calculateTotalExpenses(expenses.get())))
                    .expensesList(expenses.get())
                    .incomeList(income.get())
                    // TODO Category Expenses to be implemented
                    .build();
        }
        return null;
    }


}
