package pl.sonmiike.reportsservice.report.assemblers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.category.Category;
import pl.sonmiike.reportsservice.category.CategoryCalculator;
import pl.sonmiike.reportsservice.category.CategoryService;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.expense.ExpenseFetcher;
import pl.sonmiike.reportsservice.income.Income;
import pl.sonmiike.reportsservice.income.IncomeFetcher;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.MonthlyReport;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MonthlyReportAssemblerTest {

    @Mock
    private IncomeFetcher incomeFetcher;
    @Mock
    private ExpenseFetcher expenseFetcher;
    @Mock
    private CategoryCalculator categoryCalculator;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private MonthlyReportAssembler reportAssembler;

    private UserReport user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserReport(1L);
    }

    @Test
    void testCreateReportWithNoData() {
        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).minusDays(1);
        DateInterval dateInterval = new DateInterval(startDate, endDate);

        when(incomeFetcher.fetchSortedIncomes(eq(dateInterval), eq(user.getUserId()))).thenReturn(Collections.emptyList());
        when(expenseFetcher.fetchSortedExpenses(eq(dateInterval), eq(user.getUserId()))).thenReturn(Collections.emptyList());
        when(categoryService.getCategories()).thenReturn(Collections.emptyList());
        when(categoryCalculator.calculateCategoryExpenses(any(), any())).thenReturn(new HashMap<>());

        Report report = reportAssembler.createReport(user, new HashMap<>());

        assertNull(report, "Report should be null when there are no incomes and expenses");
        verify(incomeFetcher).fetchSortedIncomes(eq(dateInterval), eq(user.getUserId()));
        verify(expenseFetcher).fetchSortedExpenses(eq(dateInterval), eq(user.getUserId()));
        verify(categoryCalculator, never()).calculateCategoryExpenses(any(), any());
    }

    @Test
    void testCreateReportWithData() {
        LocalDate startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endDate = LocalDate.now().withDayOfMonth(1).minusDays(1);
        DateInterval dateInterval = new DateInterval(startDate, endDate);

        List<Income> incomes = getIncomes();
        List<Expense> expenses = getExpenses();

        when(categoryService.getCategories()).thenReturn(Collections.singletonList(getCategory()));
        when(incomeFetcher.fetchSortedIncomes(eq(dateInterval), eq(user.getUserId()))).thenReturn(incomes);
        when(expenseFetcher.fetchSortedExpenses(eq(dateInterval), eq(user.getUserId()))).thenReturn(expenses);
        when(categoryCalculator.calculateCategoryExpenses(any(), any())).thenReturn(new HashMap<>());

        Report report = reportAssembler.createReport(user, new HashMap<>());

        assertNotNull(report, "Report should not be null when data is present");
        assertInstanceOf(MonthlyReport.class, report, "Report should be an instance of MonthlyReport");
        verify(incomeFetcher).fetchSortedIncomes(eq(dateInterval), eq(user.getUserId()));
        verify(expenseFetcher).fetchSortedExpenses(eq(dateInterval), eq(user.getUserId()));
        verify(categoryCalculator).calculateCategoryExpenses(any(), any());
    }


    private List<Income> getIncomes() {
        Income income = new Income(1L, LocalDate.now(), "Salary", "Monthly salary", BigDecimal.valueOf(7803), user);
        Income income2 = new Income(2L, LocalDate.now(), "Second Salary", "Second Monthly salary", BigDecimal.valueOf(1234), user);
        return List.of(income, income2);
    }

    private List<Expense> getExpenses() {
        Category category = getCategory();  // Reuse the same category instance for all expenses
        Expense expense = new Expense(1L, "Rent", "Monthly rent", LocalDate.now(), BigDecimal.valueOf(2000), user, category);
        Expense expense2 = new Expense(2L, "Food", "Groceries", LocalDate.now(), BigDecimal.valueOf(240), user, category);
        return List.of(expense, expense2);
    }

    private Category getCategory() {
        return new Category(1L, "Utilities");
    }
}