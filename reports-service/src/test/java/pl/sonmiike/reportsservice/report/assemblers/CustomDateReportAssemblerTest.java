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
import pl.sonmiike.reportsservice.report.types.CustomDateReport;
import pl.sonmiike.reportsservice.report.types.DateInterval;
import pl.sonmiike.reportsservice.report.types.Report;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomDateReportAssemblerTest {

    @Mock
    private IncomeFetcher incomeFetcher;
    @Mock
    private ExpenseFetcher expenseFetcher;

    @Mock
    private CategoryCalculator categoryCalculator;
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CustomDateReportAssembler assembler;

    @Captor
    ArgumentCaptor<DateInterval> dateIntervalCaptor;

    private UserReport user;
    private LocalDate startDate, endDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserReport(1L);
        startDate = LocalDate.of(2022, 1, 1);
        endDate = LocalDate.of(2022, 1, 31);
    }

    @Test
    void testCreateReportWithEmptyData() {


        when(categoryService.getCategories()).thenReturn(Collections.emptyList());
        when(incomeFetcher.fetchSortedIncomes(any(DateInterval.class), eq(1L))).thenReturn(Collections.emptyList());
        when(expenseFetcher.fetchSortedExpenses(any(DateInterval.class), eq(1L))).thenReturn(Collections.emptyList());

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        Report report = assembler.createReport(user, params);

        assertNull(report, "Report should be null for empty incomes and expenses");

        verify(incomeFetcher).fetchSortedIncomes(dateIntervalCaptor.capture(), eq(1L));
        verify(expenseFetcher).fetchSortedExpenses(dateIntervalCaptor.capture(), eq(1L));

        DateInterval capturedInterval = dateIntervalCaptor.getValue();
        assertEquals(startDate, capturedInterval.getStartDate());
        assertEquals(endDate, capturedInterval.getEndDate());
    }

    @Test
    void testCreateReportWithData() {
        DateInterval dateInterval = new DateInterval(startDate, endDate);
        List<Income> incomes = getIncomes();
        List<Expense> expenses = getExpenses();

        HashMap<Category, BigDecimal> categoryExpenses = new HashMap<>();
        categoryExpenses.put(getCategory(), getExpenses().get(0).getAmount());
        categoryExpenses.put(getCategory(), getExpenses().get(1).getAmount());

        when(categoryService.getCategories()).thenReturn(List.of(getCategory()));
        when(incomeFetcher.fetchSortedIncomes(any(), eq(user.getUserId()))).thenReturn(incomes);
        when(expenseFetcher.fetchSortedExpenses(any(), eq(user.getUserId()))).thenReturn(expenses);
        when(categoryCalculator.calculateCategoryExpenses(anyList(), anyList())).thenReturn(categoryExpenses);

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        Report report = assembler.createReport(user, params);

        BigDecimal totalIncomes = incomes.stream().map(Income::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpenses = expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);


        assertNotNull(report, "Report should not be null when data is present");
        assertInstanceOf(CustomDateReport.class, report);
        CustomDateReport customReport = (CustomDateReport) report;
        assertEquals(totalIncomes, customReport.getTotalIncomes());
        assertEquals(totalExpenses, customReport.getTotalExpenses());
        assertEquals(totalIncomes.subtract(totalExpenses), customReport.getBudgetSummary());

        verify(incomeFetcher).fetchSortedIncomes(eq(dateInterval), eq(user.getUserId()));
        verify(expenseFetcher).fetchSortedExpenses(eq(dateInterval), eq(user.getUserId()));
        verify(categoryCalculator).calculateCategoryExpenses(eq(expenses), eq(List.of(getCategory())));
    }


    private List<Income> getIncomes() {
        Income income = new Income(1L, LocalDate.now(), "Salary", "Monthly salary", BigDecimal.valueOf(7803), user);
        Income income2 = new Income(2L, LocalDate.now(), "Second Salary", "Second Monthly salary", BigDecimal.valueOf(1234), user);
        return List.of(income, income2);
    }

    private List<Expense> getExpenses() {
        Expense expense = new Expense(1L, "Rent", "Monthly rent", LocalDate.now(), BigDecimal.valueOf(2000), user, getCategory());
        Expense expense2 = new Expense(2L, "Food", "Groceries", LocalDate.now(), BigDecimal.valueOf(240), user, getCategory());
        return List.of(expense, expense2);
    }

    private Category getCategory() {
        return new Category(1L, "Utilities");
    }
}
