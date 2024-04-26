package pl.sonmiike.reportsservice.expenses;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.expense.ExpenseRepository;
import pl.sonmiike.reportsservice.expense.ExpenseService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExpensesEntityServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private AutoCloseable openMocks;


    @BeforeEach
    void init() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void close() throws Exception {
        openMocks.close();
    }

    @Test
    void getExpensesFromDateBetween() {
        LocalDate startDate = LocalDate.of(2024, 4, 20);
        LocalDate endDate = LocalDate.of(2024, 4, 27);
        Long userId = 1L;

        List<Expense> expenseEntities = List.of(new Expense());

        when(expenseService.getExpensesFromDateBetween(eq(startDate), eq(endDate), anyLong())).thenReturn(Optional.of(expenseEntities));

        Optional<List<Expense>> result = expenseService.getExpensesFromDateBetween(startDate, endDate, userId);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        verify(expenseRepository).findExpenseEntitiesByDateBetweenAndUserUserId(startDate, endDate, userId);
    }
}
