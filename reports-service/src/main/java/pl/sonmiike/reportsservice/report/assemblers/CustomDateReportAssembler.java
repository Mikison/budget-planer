package pl.sonmiike.reportsservice.report.assemblers;

import lombok.Getter;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.category.CategoryCalculator;
import pl.sonmiike.reportsservice.category.CategoryService;
import pl.sonmiike.reportsservice.expense.ExpenseFetcher;
import pl.sonmiike.reportsservice.income.IncomeFetcher;
import pl.sonmiike.reportsservice.report.types.CustomDateReport;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.user.UserReport;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

import static pl.sonmiike.reportsservice.expense.ExpenseOperations.*;
import static pl.sonmiike.reportsservice.income.IncomeOperations.getTotalIncomes;


@Component
@Getter
public class CustomDateReportAssembler extends BaseReportAssembler implements ReportAssembler {

    LocalDate startDate;
    LocalDate endDate;


    public CustomDateReportAssembler(IncomeFetcher incomeFetcher, ExpenseFetcher expenseFetcher, CategoryCalculator categoryCalculator, CategoryService categoryService) {
        super(incomeFetcher, expenseFetcher, categoryCalculator, categoryService);
    }

    @Override
    protected DateInterval getDateInterval() {
        return new DateInterval(this.startDate, this.endDate);
    }


    public Report createReport(UserReport user, Map<String, Object> parameters) {
        LocalDate startDate = (LocalDate) parameters.get("startDate");
        LocalDate endDate = (LocalDate) parameters.get("endDate");
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
