package pl.sonmiike.reportsservice.expenses;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.expense.ExpenseDataFetcher;
import pl.sonmiike.reportsservice.expense.ExpenseService;
import pl.sonmiike.reportsservice.report.types.DateInterval;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseDataFetcherTest {

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseDataFetcher expenseDataFetcher;


    @Test
    void testFetchSortedExpenses() {
        LocalDate startDate = LocalDate.of(2022, 1, 10);
        LocalDate endDate = LocalDate.of(2022, 1, 20);
        Long userId = 1L;
        DateInterval interval = new DateInterval(startDate, endDate);

        Expense exp1 = new Expense(1L, "Groceries", "Shopping", LocalDate.of(2022, 1, 15), BigDecimal.valueOf(200), null, null);
        Expense exp2 = new Expense(2L, "Electricity", "Electricity bill", LocalDate.of(2022, 1, 12), BigDecimal.valueOf(100), null, null);
        List<Expense> expenses = Arrays.asList(exp1, exp2);

        when(expenseService.getExpensesFromDateBetween(startDate, endDate, userId))
                .thenReturn(Optional.of(expenses));

        List<Expense> sortedExpenses = expenseDataFetcher.fetchSortedExpenses(interval, userId);

        assertEquals(2, sortedExpenses.size(), "Should return two expenses");
        assertEquals(exp2, sortedExpenses.get(0), "Should sort by date correctly");
        assertEquals(exp1, sortedExpenses.get(1), "Should sort by date correctly");

        verify(expenseService).getExpensesFromDateBetween(startDate, endDate, userId);
    }
}