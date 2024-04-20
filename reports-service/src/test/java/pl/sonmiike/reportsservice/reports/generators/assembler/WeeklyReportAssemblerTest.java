package pl.sonmiike.reportsservice.reports.generators.assembler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.cateogry.CategoryEntity;
import pl.sonmiike.reportsservice.cateogry.CategoryEntityService;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.expense.ExpenseEntityService;
import pl.sonmiike.reportsservice.income.IncomeEntity;
import pl.sonmiike.reportsservice.income.IncomeEntityService;
import pl.sonmiike.reportsservice.report.generators.assemblers.WeeklyReportAssembler;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class WeeklyReportAssemblerTest {

    @Mock
    private IncomeEntityService incomeEntityService;
    @Mock
    private ExpenseEntityService expenseEntityService;
    @Mock
    private CategoryEntityService categoryEntityService;

    @InjectMocks
    private WeeklyReportAssembler weeklyReportAssembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWeeklyReport_WithData() {
        UserEntityReport user = getUser();
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
        LocalDate endDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        DateInterval dateInterval = new DateInterval(startDate, endDate);

        List<IncomeEntity> incomes = getIncomes();
        List<ExpenseEntity> expenses = getExpenses();
        List<CategoryEntity> categories = List.of(getCategory());

        when(incomeEntityService.getIncomesFromDateInterval(startDate, endDate, user.getUserId())).thenReturn(Optional.of(incomes));
        when(expenseEntityService.getExpensesFromDateBetween(startDate, endDate, user.getUserId())).thenReturn(Optional.of(expenses));
        when(categoryEntityService.getCategories()).thenReturn(categories);

        WeeklyReport report = weeklyReportAssembler.createWeeklyReport(user);

        assertNotNull(report);
        assertEquals(BigDecimal.valueOf(1000), report.getTotalIncomes());
        assertEquals(BigDecimal.valueOf(100), report.getTotalExpenses());
        assertEquals(BigDecimal.valueOf(900), report.getBudgetSummary());
        assertFalse(report.getCategoryExpenses().isEmpty());
    }

    @Test
    void testCreateWeeklyReport_NoData() {
        UserEntityReport user = new UserEntityReport(1L);
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
        LocalDate endDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        when(incomeEntityService.getIncomesFromDateInterval(startDate, endDate, user.getUserId())).thenReturn(Optional.empty());
        when(expenseEntityService.getExpensesFromDateBetween(startDate, endDate, user.getUserId())).thenReturn(Optional.empty());
        when(categoryEntityService.getCategories()).thenReturn(Collections.emptyList());

        WeeklyReport report = weeklyReportAssembler.createWeeklyReport(user);

        assertNull(report);
    }


    private UserEntityReport getUser() {
        return new UserEntityReport(1L);
    }

    private CategoryEntity getCategory() {
        return new CategoryEntity(1L, "Shopping");
    }

    private List<ExpenseEntity> getExpenses() {
        return List.of(new ExpenseEntity(
                1L,
                "Groceries",
                "Weekly Groceries",
                LocalDate.now().minusDays(3),
                BigDecimal.valueOf(100),
                getUser(),
                getCategory()
        ));
    }

    private List<IncomeEntity> getIncomes() {
        return List.of(new IncomeEntity(
                1L,
                LocalDate.now().minusDays(3),
                "Monthly Salary",
                "May",
                BigDecimal.valueOf(1000),
                getUser()
        ));
    }
}
