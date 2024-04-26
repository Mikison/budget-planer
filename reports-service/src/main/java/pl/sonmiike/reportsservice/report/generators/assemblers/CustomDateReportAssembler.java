package pl.sonmiike.reportsservice.report.generators.assemblers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.expense.ExpenseService;
import pl.sonmiike.reportsservice.income.IncomeService;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserReportService;

import java.time.LocalDate;


@Component
@RequiredArgsConstructor
public class CustomDateReportAssembler {


    private final UserReportService userReportService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    // TODO Implement this method
    public WeeklyReport createCustomDateIntervalReport(Long userId, LocalDate startDate, LocalDate endDate) {
        return null;
    }


}
