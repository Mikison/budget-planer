package pl.sonmiike.reportsservice.reports.generators.assembler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.category.Category;
import pl.sonmiike.reportsservice.category.CategoryService;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.expense.ExpenseService;
import pl.sonmiike.reportsservice.income.Income;
import pl.sonmiike.reportsservice.income.IncomeService;
import pl.sonmiike.reportsservice.report.assemblers.WeeklyReportAssembler;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
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
    void testCreateReport_WithData() {
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

        Report report = weeklyReportAssembler.createReport(user);

//        assertNotNull(report);
//        assertEquals(BigDecimal.valueOf(1000), report.getTotalIncomes());
//        assertEquals(BigDecimal.valueOf(100), report.getTotalExpenses());
//        assertEquals(BigDecimal.valueOf(900), report.getBudgetSummary());
//        assertFalse(report.getCategoryExpenses().isEmpty());
    }

    @Test
    void testCreateReport_NoData() {
        UserReport user = getUser();
        LocalDate startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
        LocalDate endDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        when(incomeService.getIncomesFromDateInterval(startDate, endDate, user.getUserId())).thenReturn(Optional.empty());
        when(expenseService.getExpensesFromDateBetween(startDate, endDate, user.getUserId())).thenReturn(Optional.empty());
        when(categoryService.getCategories()).thenReturn(Collections.emptyList());

        Report report = weeklyReportAssembler.createReport(user);

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
