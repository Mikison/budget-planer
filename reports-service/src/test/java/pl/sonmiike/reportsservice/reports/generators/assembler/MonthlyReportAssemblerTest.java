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
import pl.sonmiike.reportsservice.report.generators.assemblers.MonthlyReportAssembler;
import pl.sonmiike.reportsservice.report.types.MonthlyReport;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class MonthlyReportAssemblerTest {

    @Mock
    private IncomeService incomeService;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private MonthlyReportAssembler monthlyReportAssembler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMonthlyReport_WithData() {
        UserReport user = getUser();
        List<Income> incomes = getIncomes();
        List<Expense> expenses = getExpenses();
        List<Category> categories = List.of(getCategory());

        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).minusDays(1);

        when(incomeService.getIncomesFromDateInterval(startDate, endDate, user.getUserId())).thenReturn(Optional.of(incomes));
        when(expenseService.getExpensesFromDateBetween(startDate, endDate, user.getUserId())).thenReturn(Optional.of(expenses));
        when(categoryService.getCategories()).thenReturn(categories);

        MonthlyReport report = monthlyReportAssembler.createMonthlyReport(user);

        assertNotNull(report);
        assertEquals(BigDecimal.valueOf(1000), report.getTotalIncomes());
        assertEquals(BigDecimal.valueOf(100), report.getTotalExpenses());
        assertEquals(BigDecimal.valueOf(1000).subtract(BigDecimal.valueOf(100)), report.getBudgetSummary());
        assertFalse(report.getCategoryExpenses().isEmpty());
    }

    @Test
    void testCreateMonthlyReport_NoData() {
        UserReport user = getUser();
        when(incomeService.getIncomesFromDateInterval(any(), any(), anyLong())).thenReturn(Optional.empty());
        when(expenseService.getExpensesFromDateBetween(any(), any(), anyLong())).thenReturn(Optional.empty());
        when(categoryService.getCategories()).thenReturn(new ArrayList<>());

        MonthlyReport report = monthlyReportAssembler.createMonthlyReport(user);

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

