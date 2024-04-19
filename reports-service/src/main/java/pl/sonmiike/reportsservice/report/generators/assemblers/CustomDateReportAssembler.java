package pl.sonmiike.reportsservice.report.generators.assemblers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.expense.ExpenseEntityService;
import pl.sonmiike.reportsservice.income.IncomeEntityService;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserEntityService;

import java.time.LocalDate;


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
