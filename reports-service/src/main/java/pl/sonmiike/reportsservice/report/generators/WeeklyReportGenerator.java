package pl.sonmiike.reportsservice.report.generators;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.expense.ExpenseEntityService;
import pl.sonmiike.reportsservice.income.IncomeEntity;
import pl.sonmiike.reportsservice.income.IncomeService;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserEntityReport;
import pl.sonmiike.reportsservice.user.refrshtoken.UserEntityService;

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
    private final IncomeService incomeService;
    private final ExpenseEntityService expenseEntityService;


    public GenericPDFReportGenerator<WeeklyReport> createWeeklyReport() {
        DateInterval date = getDateInterval();
        Set<WeeklyReport> weeklyReports = new HashSet<>();
        for (UserEntityReport user : userEntityService.getAllUsers()) {
            Optional<List<IncomeEntity>> income = incomeService.getIncomesFromDateInterval(date.getStartDate(), date.getEndDate(), user.getUserId());
            Optional<List<ExpenseEntity>> expenses = expenseEntityService.getExpensesFromDateBetween(date.getStartDate(), date.getEndDate(), user.getUserId());
            if (income.isPresent() && expenses.isPresent()) {
                WeeklyReport weeklyReport = WeeklyReport.builder()
                        .user(user)
                        .dateInterval(date)
                        .totalExpenses(calculateTotalExpenses(expenses.get()))
                        .biggestExpense(findMaxExpense(expenses.get()))
                        .smallestExpense(findMinExpense(expenses.get()))
                        .averageDailyExpense(calculateAverageDailyExpenses(expenses.get(), Period.between(date.getStartDate(), date.getEndDate()).getDays() + 1))
                        .totalIncomes(getTotalIncomes(income.get()))
                        // TODO Percentage of Budget Spent to be implemented
                        .budgetSummary(getTotalIncomes(income.get()).subtract(calculateTotalExpenses(expenses.get())))
                        .expensesList(expenses.get())
                        .incomeList(income.get())
                        // TODO Category Expenses to be implemented
                        .build();
                weeklyReports.add(weeklyReport);
            }
        }
        return new GenericPDFReportGenerator<>(weeklyReports);

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
