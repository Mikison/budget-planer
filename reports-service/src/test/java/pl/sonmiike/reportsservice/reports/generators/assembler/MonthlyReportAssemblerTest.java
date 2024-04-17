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
import pl.sonmiike.reportsservice.report.generators.assemblers.MonthlyReportAssembler;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.MonthlyReport;
import pl.sonmiike.reportsservice.user.UserEntityReport;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class MonthlyReportAssemblerTest {

    @Mock
    private IncomeEntityService incomeEntityService;

    @Mock
    private ExpenseEntityService expenseEntityService;

    @Mock
    private CategoryEntityService categoryEntityService;

    @InjectMocks
    private MonthlyReportAssembler monthlyReportAssembler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateMonthlyReport_WithData() {
        UserEntityReport user = getUser();
        List<IncomeEntity> incomes = getIncomes();
        List<ExpenseEntity> expenses = getExpenses();
        List<CategoryEntity> categories = List.of(getCategory());

        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).minusDays(1);

        when(incomeEntityService.getIncomesFromDateInterval(startDate, endDate, user.getUserId())).thenReturn(Optional.of(incomes));
        when(expenseEntityService.getExpensesFromDateBetween(startDate, endDate, user.getUserId())).thenReturn(Optional.of(expenses));
        when(categoryEntityService.getCategories()).thenReturn(categories);

        MonthlyReport report = monthlyReportAssembler.createMonthlyReport(user);

        assertNotNull(report);
        assertEquals(BigDecimal.valueOf(1000), report.getTotalIncomes());
        assertEquals(BigDecimal.valueOf(100), report.getTotalExpenses());
        assertEquals(BigDecimal.valueOf(1000).subtract(BigDecimal.valueOf(100)), report.getBudgetSummary());
        assertFalse(report.getCategoryExpenses().isEmpty());
    }

    @Test
    public void testCreateMonthlyReport_NoData() {
        UserEntityReport user = getUser();
        when(incomeEntityService.getIncomesFromDateInterval(any(), any(), anyLong())).thenReturn(Optional.empty());
        when(expenseEntityService.getExpensesFromDateBetween(any(), any(), anyLong())).thenReturn(Optional.empty());
        when(categoryEntityService.getCategories()).thenReturn(new ArrayList<>());

        MonthlyReport report = monthlyReportAssembler.createMonthlyReport(user);

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

