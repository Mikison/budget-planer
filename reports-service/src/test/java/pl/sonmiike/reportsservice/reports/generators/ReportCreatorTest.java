package pl.sonmiike.reportsservice.reports.generators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.income.Income;
import pl.sonmiike.reportsservice.report.ReportMailSender;
import pl.sonmiike.reportsservice.report.database.ReportType;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;
import pl.sonmiike.reportsservice.report.generators.ReportGenerator;
import pl.sonmiike.reportsservice.report.generators.assemblers.MonthlyReportAssembler;
import pl.sonmiike.reportsservice.report.generators.assemblers.ReportAssembler;
import pl.sonmiike.reportsservice.report.generators.assemblers.ReportAssemblerFactory;
import pl.sonmiike.reportsservice.report.generators.assemblers.WeeklyReportAssembler;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserReport;
import pl.sonmiike.reportsservice.user.UserReportService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportCreatorTest {

    @Mock
    private UserReportService userReportService;


    @Mock
    private ReportAssemblerFactory reportAssemblerFactory;

    @Mock
    private ReportGenerator<Report> reportGenerator;

    @Mock
    private ReportMailSender reportMailSender;

    @InjectMocks
    private ReportCreator reportCreator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGenerateReport_WhenUserIsNull() {
        Long userId = 1L;

        when(userReportService.fetchUserById(userId)).thenReturn(Optional.empty());

        reportCreator.generateReport(userId, any(ReportType.class));

        verify(userReportService).fetchUserById(userId);
    }

    @Test
    void testGenerateReport_WhenValidUserAndReportNull() {
        Long userId = 1L;
        UserReport user = getUser();

        when(userReportService.fetchUserById(userId)).thenReturn(Optional.ofNullable(user));
        when(weeklyReportAssembler.createReport(user)).thenReturn(null);

        reportCreator.generateReport(userId, ReportType.WEEKLY_REPORT);

        verify(userReportService).fetchUserById(userId);
        verify(weeklyReportAssembler).createReport(user);
    }


    @Test
    void testGenerateReport_WhenValidUserAndReportNotNull() {
        Long userId = 1L;
        UserReport user = getUser();
        WeeklyReport weeklyReport = getWeeklyReport();

        when(userReportService.fetchUserById(userId)).thenReturn(Optional.ofNullable(user));
        when(weeklyReportAssembler.createReport(user)).thenReturn(weeklyReport);
        when(reportGenerator.generatePDF(weeklyReport)).thenReturn("test.pdf");

        reportCreator.generateReport(userId, ReportType.WEEKLY_REPORT);

        verify(userReportService).fetchUserById(userId);
        verify(weeklyReportAssembler).createReport(user);
        verify(reportGenerator).generatePDF(weeklyReport);
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

