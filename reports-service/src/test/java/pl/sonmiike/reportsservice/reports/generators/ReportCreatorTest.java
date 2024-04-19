package pl.sonmiike.reportsservice.reports.generators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.report.generators.ReportCreator;
import pl.sonmiike.reportsservice.report.generators.assemblers.MonthlyReportAssembler;
import pl.sonmiike.reportsservice.report.generators.assemblers.WeeklyReportAssembler;
import pl.sonmiike.reportsservice.report.types.MonthlyReport;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserEntityReport;
import pl.sonmiike.reportsservice.user.UserEntityService;

import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class ReportCreatorTest {

    @Mock
    private UserEntityService userEntityService;

    @Mock
    private WeeklyReportAssembler weeklyReportAssembler;

    @Mock
    private MonthlyReportAssembler monthlyReportAssembler;

    @InjectMocks
    private ReportCreator reportCreator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGenerateWeeklyReport_WhenReportIsCreated() {
        Long userId = 1L;
        UserEntityReport user = getUser(); // Assuming existence of such a class
        WeeklyReport weeklyReport = mock(WeeklyReport.class);
        weeklyReport.setUser(user);

        when(userEntityService.getUserById(userId)).thenReturn(user);
        when(weeklyReportAssembler.createWeeklyReport(user)).thenReturn(weeklyReport);

        reportCreator.generateWeeklyReport(userId);

        verify(userEntityService).getUserById(userId);
        verify(weeklyReportAssembler).createWeeklyReport(user);

    }

    @Test
    public void testGenerateWeeklyReport_WhenReportIsNull() {
        Long userId = 1L;
        UserEntityReport user = getUser();

        when(userEntityService.getUserById(userId)).thenReturn(user);
        when(weeklyReportAssembler.createWeeklyReport(user)).thenReturn(null);

        reportCreator.generateWeeklyReport(userId);

        verify(userEntityService).getUserById(userId);
        verify(weeklyReportAssembler).createWeeklyReport(user);

    }


    @Test
    public void testGenerateMonthlyReport_WhenReportIsCreated() {
        Long userId = 1L;
        UserEntityReport user = getUser();
        MonthlyReport monthlyReport = mock(MonthlyReport.class);
        monthlyReport.setUser(user);

        when(userEntityService.getUserById(userId)).thenReturn(user);
        when(monthlyReportAssembler.createMonthlyReport(user)).thenReturn(monthlyReport);

        reportCreator.generateMonthlyReport(userId);

        verify(userEntityService).getUserById(userId);
        verify(monthlyReportAssembler).createMonthlyReport(user);

    }

    @Test
    public void testGenerateMonthlyReport_WhenReportIsNull() {
        Long userId = 1L;
        UserEntityReport user = getUser();

        when(userEntityService.getUserById(userId)).thenReturn(user);
        when(monthlyReportAssembler.createMonthlyReport(user)).thenReturn(null);

        reportCreator.generateMonthlyReport(userId);

        verify(userEntityService).getUserById(userId);
        verify(monthlyReportAssembler).createMonthlyReport(user);

    }

    private UserEntityReport getUser() {
        return UserEntityReport.builder()
                .userId(1L)
                .username("testUser")
                .email("test@test.com")
                .build();
    }


}

