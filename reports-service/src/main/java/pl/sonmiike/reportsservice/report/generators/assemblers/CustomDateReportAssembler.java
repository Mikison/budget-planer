package pl.sonmiike.reportsservice.report.generators.assemblers;

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
public class CustomDateReportAssembler {


    private final UserEntityService userEntityService;
    private final IncomeEntityService incomeEntityService;
    private final ExpenseEntityService expenseEntityService;

    // TODO Implement this method
    public WeeklyReport createCustomDateIntervalReport(Long userId, LocalDate startDate, LocalDate endDate) {
        return null;
    }


}
