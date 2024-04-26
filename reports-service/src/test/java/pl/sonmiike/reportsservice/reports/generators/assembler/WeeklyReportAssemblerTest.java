package pl.sonmiike.reportsservice.reports.generators.assembler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.cateogry.Category;
import pl.sonmiike.reportsservice.cateogry.CategoryService;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.expense.ExpenseService;
import pl.sonmiike.reportsservice.income.Income;
import pl.sonmiike.reportsservice.income.IncomeService;
import pl.sonmiike.reportsservice.report.generators.assemblers.WeeklyReportAssembler;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserReport;

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
    private IncomeService incomeService;
    @Mock
    private ExpenseService expenseService;
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private WeeklyReportAssembler weeklyReportAssembler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWeeklyReport_WithData() {
        UserReport user = getUser();
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
        LocalDate endDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        DateInterval dateInterval = new DateInterval(startDate, endDate);

        List<Income> incomes = getIncomes();
        List<Expense> expenses = getExpenses();
        List<Category> categories = List.of(getCategory());

        when(incomeService.getIncomesFromDateInterval(startDate, endDate, user.getUserId())).thenReturn(Optional.of(incomes));
        when(expenseService.getExpensesFromDateBetween(startDate, endDate, user.getUserId())).thenReturn(Optional.of(expenses));
        when(categoryService.getCategories()).thenReturn(categories);

        WeeklyReport report = weeklyReportAssembler.createWeeklyReport(user);

        assertNotNull(report);
        assertEquals(BigDecimal.valueOf(1000), report.getTotalIncomes());
        assertEquals(BigDecimal.valueOf(100), report.getTotalExpenses());
        assertEquals(BigDecimal.valueOf(900), report.getBudgetSummary());
        assertFalse(report.getCategoryExpenses().isEmpty());
    }

    @Test
    void testCreateWeeklyReport_NoData() {
        UserReport user = new UserReport(1L);
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
        LocalDate endDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        when(incomeService.getIncomesFromDateInterval(startDate, endDate, user.getUserId())).thenReturn(Optional.empty());
        when(expenseService.getExpensesFromDateBetween(startDate, endDate, user.getUserId())).thenReturn(Optional.empty());
        when(categoryService.getCategories()).thenReturn(Collections.emptyList());

        WeeklyReport report = weeklyReportAssembler.createWeeklyReport(user);

        assertNull(report);
    }


    private UserReport getUser() {
        return new UserReport(1L);
    }

    private Category getCategory() {
        return new Category(1L, "Shopping");
    }

    private List<Expense> getExpenses() {
        return List.of(new Expense(
                1L,
                "Groceries",
                "Weekly Groceries",
                LocalDate.now().minusDays(3),
                BigDecimal.valueOf(100),
                getUser(),
                getCategory()
        ));
    }

    private List<Income> getIncomes() {
        return List.of(new Income(
                1L,
                LocalDate.now().minusDays(3),
                "Monthly Salary",
                "May",
                BigDecimal.valueOf(1000),
                getUser()
        ));
    }
}
