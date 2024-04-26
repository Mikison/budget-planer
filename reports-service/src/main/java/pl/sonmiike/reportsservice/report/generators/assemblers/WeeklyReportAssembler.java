package pl.sonmiike.reportsservice.report.generators.assemblers;


import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.cateogry.CategoryService;
import pl.sonmiike.reportsservice.expense.ExpenseService;
import pl.sonmiike.reportsservice.income.IncomeService;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserReport;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;

import static pl.sonmiike.reportsservice.expense.ExpenseOperations.*;
import static pl.sonmiike.reportsservice.income.IncomeOperations.getTotalIncomes;

@Component
public class WeeklyReportAssembler extends BaseReportAssembler {

    public WeeklyReportAssembler(IncomeService incomeService, ExpenseService expenseService, CategoryService categoryService) {
        super(incomeService, expenseService, categoryService);
    }

    @Override
    protected DateInterval getDateInterval() {
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
        LocalDate endDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return new DateInterval(startDate, endDate);
    }

    public WeeklyReport createWeeklyReport(UserReport user) {
        return createReport(user, (userDetails, date, incomes, expenses, categoryExpenses) -> WeeklyReport.builder()
                .user(userDetails)
                .dateInterval(date)
                .totalExpenses(calculateTotalExpenses(expenses))
                .biggestExpense(findMaxExpense(expenses))
                .smallestExpense(findMinExpense(expenses))
                .averageDailyExpense(calculateAverageDailyExpenses(expenses, Period.between(date.getStartDate(), date.getEndDate()).getDays() + 1))
                .totalIncomes(getTotalIncomes(incomes))
                .budgetSummary(getTotalIncomes(incomes).subtract(calculateTotalExpenses(expenses)))
                .expensesList(expenses)
                .incomeList(incomes)
                .categoryExpenses(categoryExpenses)
                .build());
    }
}

