package pl.sonmiike.reportsservice.reports.generators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.income.Income;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;
import pl.sonmiike.reportsservice.report.generators.ReportGenerator;
import pl.sonmiike.reportsservice.report.generators.assemblers.MonthlyReportAssembler;
import pl.sonmiike.reportsservice.report.generators.assemblers.WeeklyReportAssembler;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserReport;
import pl.sonmiike.reportsservice.user.UserReportService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class ReportCreatorTest {

    @Mock
    private UserReportService userReportService;

    @Mock
    private WeeklyReportAssembler weeklyReportAssembler;

    @Mock
    private MonthlyReportAssembler monthlyReportAssembler;

    @Mock
    private ReportGenerator<Report> reportGenerator;

    @InjectMocks
    private ReportCreator reportCreator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testGenerateWeeklyReport_WhenReportIsNull() {
        Long userId = 1L;
        UserReport user = getUser();

        when(userReportService.getUserById(userId)).thenReturn(user);
        when(weeklyReportAssembler.createWeeklyReport(user)).thenReturn(null);

        reportCreator.generateReport(userId, ReportType.WEEKLY_REPORT);

        verify(userReportService).getUserById(userId);
        verify(weeklyReportAssembler).createWeeklyReport(user);

    }



    @Test
    void testGenerateMonthlyReport_WhenReportIsNull() {
        Long userId = 1L;
        UserReport user = getUser();

        when(userReportService.getUserById(userId)).thenReturn(user);
        when(monthlyReportAssembler.createMonthlyReport(user)).thenReturn(null);

        reportCreator.generateReport(userId, ReportType.MONTHLY_REPORT);

        verify(userReportService).getUserById(userId);
        verify(monthlyReportAssembler).createMonthlyReport(user);

    }

    private UserReport getUser() {
        return UserReport.builder()
                .userId(1L)
                .username("testUser")
                .email("test@test.com")
                .build();
    }


    private WeeklyReport getWeeklyReport() {
        return WeeklyReport.builder()
                .user(getUser())
                .dateInterval(new DateInterval(LocalDate.now().minusDays(7), LocalDate.now()))
                .totalExpenses(BigDecimal.valueOf(100))
                .biggestExpense(new Expense())
                .smallestExpense(new Expense())
                .averageDailyExpense(BigDecimal.valueOf(10))
                .totalIncomes(BigDecimal.valueOf(200))
                .budgetSummary(BigDecimal.valueOf(100))
                .expensesList(List.of(new Expense()))
                .incomeList(List.of(new Income()))
                .categoryExpenses(null)
                .build();
    }

}

