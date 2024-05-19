package pl.sonmiike.reportsservice.report.assemblers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
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
import static org.mockito.Mockito.*;

class WeeklyReportAssemblerTest {

    @Mock
    private IncomeFetcher incomeFetcher;
    @Mock
    private ExpenseFetcher expenseFetcher;
    @Mock
    private CategoryCalculator categoryCalculator;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private WeeklyReportAssembler assembler;

    @Captor
    private ArgumentCaptor<DateInterval> dateIntervalCaptor;

    private UserReport user;
    private LocalDate referenceDate;
    private DateInterval dateInterval;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserReport(1L);
        referenceDate = LocalDate.of(2024, 5, 12).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).minusWeeks(1);
        dateInterval = new DateInterval(referenceDate, referenceDate.plusDays(7));
    }

    @Test
    void testCreateReportWithData() {
        List<Income> incomes = getIncomes();
        List<Expense> expenses = getExpenses();

        when(incomeFetcher.fetchSortedIncomes(any(), eq(user.getUserId()))).thenReturn(incomes);
        when(expenseFetcher.fetchSortedExpenses(any(), eq(user.getUserId()))).thenReturn(expenses);
        when(categoryService.getCategories()).thenReturn(Collections.singletonList(getCategory()));
        when(categoryCalculator.calculateCategoryExpenses(any(), any())).thenReturn(new HashMap<>());

        Report report = assembler.createReport(user, new HashMap<>());

        assertNotNull(report, "Report should not be null when data is present");
        assertInstanceOf(WeeklyReport.class, report, "Report should be an instance of WeeklyReport");

        verify(incomeFetcher).fetchSortedIncomes(dateIntervalCaptor.capture(), eq(user.getUserId()));
        verify(expenseFetcher).fetchSortedExpenses(dateIntervalCaptor.capture(), eq(user.getUserId()));

        DateInterval capturedIntervalForIncome = dateIntervalCaptor.getAllValues().get(0);
        DateInterval capturedIntervalForExpense = dateIntervalCaptor.getAllValues().get(1);

        assertAll("DateInterval properties should match",
                () -> assertEquals(dateInterval.getStartDate(), capturedIntervalForIncome.getStartDate(), "Start dates should match"),
                () -> assertEquals(dateInterval.getEndDate(), capturedIntervalForIncome.getEndDate(), "End dates should match"),
                () -> assertEquals(dateInterval.getStartDate(), capturedIntervalForExpense.getStartDate(), "Start dates should match for expense"),
                () -> assertEquals(dateInterval.getEndDate(), capturedIntervalForExpense.getEndDate(), "End dates should match for expense")
        );
    }

    @Test
    void testCreateReportWithNoData() {
        when(incomeFetcher.fetchSortedIncomes(any(), eq(user.getUserId()))).thenReturn(Collections.emptyList());
        when(expenseFetcher.fetchSortedExpenses(any(), eq(user.getUserId()))).thenReturn(Collections.emptyList());
        when(categoryService.getCategories()).thenReturn(Collections.emptyList());
        when(categoryCalculator.calculateCategoryExpenses(any(), any())).thenReturn(new HashMap<>());

        Report report = assembler.createReport(user, new HashMap<>());

        assertNull(report, "Report should be null when no data is present");
        verify(incomeFetcher).fetchSortedIncomes(dateIntervalCaptor.capture(), eq(user.getUserId()));
        verify(expenseFetcher).fetchSortedExpenses(dateIntervalCaptor.capture(), eq(user.getUserId()));

        // Check the captured values if needed
    }

    private List<Income> getIncomes() {
        Income income = new Income(1L, referenceDate, "Salary", "Monthly salary", BigDecimal.valueOf(7803), user);
        Income income2 = new Income(2L, referenceDate, "Second Salary", "Second Monthly salary", BigDecimal.valueOf(1234), user);
        return List.of(income, income2);
    }

    private List<Expense> getExpenses() {
        Category category = getCategory();
        Expense expense = new Expense(1L, "Rent", "Monthly rent", referenceDate, BigDecimal.valueOf(2000), user, category);
        Expense expense2 = new Expense(2L, "Food", "Groceries", referenceDate, BigDecimal.valueOf(240), user, category);
        return List.of(expense, expense2);
    }

    private Category getCategory() {
        return new Category(1L, "Utilities");
    }
}