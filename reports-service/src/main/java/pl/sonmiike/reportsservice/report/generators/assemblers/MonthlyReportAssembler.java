package pl.sonmiike.reportsservice.report.generators.assemblers;

import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.cateogry.CategoryEntityService;
import pl.sonmiike.reportsservice.expense.ExpenseEntityService;
import pl.sonmiike.reportsservice.income.IncomeEntityService;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.MonthlyReport;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.time.LocalDate;

import static pl.sonmiike.reportsservice.expense.ExpenseOperations.*;
import static pl.sonmiike.reportsservice.income.IncomeOperations.getTotalIncomes;


@Component
public class MonthlyReportAssembler extends BaseReportAssembler {

    public MonthlyReportAssembler(IncomeEntityService incomeEntityService, ExpenseEntityService expenseEntityService, CategoryEntityService categoryEntityService) {
        super(incomeEntityService, expenseEntityService, categoryEntityService);
    }

    @Override
    protected DateInterval getDateInterval() {
        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).minusDays(1);
        return new DateInterval(startDate, endDate);
    }

    public MonthlyReport createMonthlyReport(UserEntityReport user) {
        return createReport(user, (userDetails, date, incomes, expenses, categoryExpenses) -> MonthlyReport.builder()
                .user(userDetails)
                .dateInterval(date)
                .totalExpenses(calculateTotalExpenses(expenses))
                .averageWeeklyExpense(calculateAverageWeeklyExpenses(expenses))
                .weekWithHighestExpenses(calculateWeekWithBiggestExpense(expenses))
                .dayWithHighestAverageExpense(getDayWithHighestAverageExpense(expenses))
                .totalIncomes(getTotalIncomes(incomes))
                .budgetSummary(getTotalIncomes(incomes).subtract(calculateTotalExpenses(expenses)))
                .expensesList(expenses)
                .incomeList(incomes)
                .categoryExpenses(categoryExpenses)
                .build());
    }
}
