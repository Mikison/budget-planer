package pl.sonmiike.reportsservice.report.generators.assemblers;

import lombok.Getter;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.cateogry.CategoryService;
import pl.sonmiike.reportsservice.expense.ExpenseService;
import pl.sonmiike.reportsservice.income.IncomeService;
import pl.sonmiike.reportsservice.report.types.CustomDateReport;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.user.UserReport;

import java.time.LocalDate;
import java.time.Period;

import static pl.sonmiike.reportsservice.expense.ExpenseOperations.*;
import static pl.sonmiike.reportsservice.income.IncomeOperations.getTotalIncomes;


@Component
@Getter
public class CustomDateReportAssembler extends BaseReportAssembler {

    LocalDate startDate;
    LocalDate endDate;


    public CustomDateReportAssembler(IncomeService incomeService, ExpenseService expenseService, CategoryService categoryService) {
        super(incomeService, expenseService, categoryService);
    }


    @Override
    protected DateInterval getDateInterval() {
        return new DateInterval(this.startDate, this.endDate);
    }


    public CustomDateReport createCustomDateReport(UserReport user, LocalDate startDate, LocalDate endDate) {
        setCustomDates(startDate, endDate);
        return createReport(user, (userDetails, date, incomes, expenses, categoryExpenses) -> CustomDateReport.builder()
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

    public void setCustomDates(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
