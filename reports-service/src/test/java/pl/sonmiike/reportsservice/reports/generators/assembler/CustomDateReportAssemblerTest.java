package pl.sonmiike.reportsservice.reports.generators.assembler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.sonmiike.reportsservice.category.Category;
import pl.sonmiike.reportsservice.category.CategoryService;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.expense.ExpenseService;
import pl.sonmiike.reportsservice.income.Income;
import pl.sonmiike.reportsservice.income.IncomeService;
import pl.sonmiike.reportsservice.report.assemblers.CustomDateReportAssembler;
import pl.sonmiike.reportsservice.report.types.CustomDateReport;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class CustomDateReportAssemblerTest {


    @Mock
    private IncomeService incomeService;
    @Mock
    private ExpenseService expenseService;
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CustomDateReportAssembler customDateReportAssembler;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }


    @Test
    void testCreateReport_WithData() {
        UserReport user = getUser();
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        List<Income> incomes = getIncomes();
        List<Expense> expenses = getExpenses();
        List<Category> categories = List.of(getCategory());

        when(incomeService.getIncomesFromDateInterval(startDate, endDate, user.getUserId())).thenReturn(Optional.of(incomes));
        when(expenseService.getExpensesFromDateBetween(startDate, endDate, user.getUserId())).thenReturn(Optional.of(expenses));
        when(categoryService.getCategories()).thenReturn(categories);

        CustomDateReport customDateReport = customDateReportAssembler.createReport(user, startDate, endDate);

        assertEquals(user, customDateReport.getUser());
        assertEquals(startDate, customDateReport.getDateInterval().getStartDate());
        assertEquals(endDate, customDateReport.getDateInterval().getEndDate());
        assertEquals(BigDecimal.valueOf(100), customDateReport.getTotalExpenses());
        assertEquals(BigDecimal.valueOf(1000), customDateReport.getTotalIncomes());
        assertEquals(BigDecimal.valueOf(900), customDateReport.getBudgetSummary());
        assertEquals(expenses, customDateReport.getExpensesList());
        assertEquals(incomes, customDateReport.getIncomeList());
    }

    @Test
    void testCreateReport_NoData() {
        UserReport user = getUser();
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        List<Category> categories = List.of(getCategory());

        when(incomeService.getIncomesFromDateInterval(startDate, endDate, user.getUserId())).thenReturn(Optional.empty());
        when(expenseService.getExpensesFromDateBetween(startDate, endDate, user.getUserId())).thenReturn(Optional.empty());
        when(categoryService.getCategories()).thenReturn(categories);

        CustomDateReport customDateReport = customDateReportAssembler.createReport(user, startDate, endDate);

        assertNull(customDateReport);
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
