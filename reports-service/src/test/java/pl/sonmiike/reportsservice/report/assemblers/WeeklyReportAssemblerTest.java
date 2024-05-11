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
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.report.types.WeeklyReport;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class WeeklyReportAssemblerTest {

    @Mock
    private IncomeFetcher incomeFetcher;
    @Mock private ExpenseFetcher expenseFetcher;
    @Mock private CategoryCalculator categoryCalculator;
    @Mock private CategoryService categoryService;
    @InjectMocks
    private WeeklyReportAssembler assembler;

    private UserReport user;
    private LocalDate startDate, endDate;
    private DateInterval dateInterval;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserReport(1L);
        startDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
        endDate = startDate.plusDays(6);
        dateInterval = new DateInterval(startDate, endDate);
    }

    @Test
    void testCreateReportWithData() {
        List<Income> incomes = getIncomes();
        List<Expense> expenses = getExpenses();

        when(incomeFetcher.fetchSortedIncomes(eq(dateInterval), eq(user.getUserId()))).thenReturn(incomes);
        when(expenseFetcher.fetchSortedExpenses(eq(dateInterval), eq(user.getUserId()))).thenReturn(expenses);
        when(categoryService.getCategories()).thenReturn(Collections.singletonList(getCategory()));
        when(categoryCalculator.calculateCategoryExpenses(any(), any())).thenReturn(new HashMap<>());

        Report report = assembler.createReport(user, new HashMap<>());

        assertNotNull(report, "Report should not be null when data is present");
        assertInstanceOf(WeeklyReport.class, report, "Report should be an instance of WeeklyReport");
        verify(incomeFetcher).fetchSortedIncomes(eq(dateInterval), eq(user.getUserId()));
        verify(expenseFetcher).fetchSortedExpenses(eq(dateInterval), eq(user.getUserId()));
        verify(categoryCalculator).calculateCategoryExpenses(any(), any());
    }

    @Test
    void testCreateReportWithNoData() {
        when(incomeFetcher.fetchSortedIncomes(eq(dateInterval), eq(user.getUserId()))).thenReturn(Collections.emptyList());
        when(expenseFetcher.fetchSortedExpenses(eq(dateInterval), eq(user.getUserId()))).thenReturn(Collections.emptyList());
        when(categoryService.getCategories()).thenReturn(Collections.emptyList());
        when(categoryCalculator.calculateCategoryExpenses(any(), any())).thenReturn(new HashMap<>());

        Report report = assembler.createReport(user, new HashMap<>());

        assertNull(report, "Report should be null when no data is present");
        verify(incomeFetcher).fetchSortedIncomes(eq(dateInterval), eq(user.getUserId()));
        verify(expenseFetcher).fetchSortedExpenses(eq(dateInterval), eq(user.getUserId()));
        verify(categoryCalculator, never()).calculateCategoryExpenses(any(), any());
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