package pl.sonmiike.reportsservice.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.expense.ExpenseOperations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryExpenseCalculatorTest {

//    @InjectMocks
//    private CategoryExpenseCalculator calculator;
//
//    @Mock
//    private ExpenseOperations expenseOperations;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCalculateCategoryExpenses() {
//        // Setup
//        Category category1 = new Category(1L, "Food");
//        Category category2 = new Category(2L, "Utilities");
//        Expense expense1 = new Expense(1L, );
//        Expense expense2 = new Expense(2L, BigDecimal.valueOf(200), category2);
//
//        List<Expense> expenses = Arrays.asList(expense1, expense2);
//        List<Category> categories = Arrays.asList(category1, category2);
//
//        when(expenseOperations.calculateTotalExpenses(any())).thenReturn(BigDecimal.valueOf(100), BigDecimal.valueOf(200));
//
//        // Execution
//        HashMap<Category, BigDecimal> results = calculator.calculateCategoryExpenses(expenses, categories);
//
//        // Verification
//        assertNotNull(results);
//        assertEquals(2, results.size());
//        assertEquals(BigDecimal.valueOf(100), results.get(category1));
//        assertEquals(BigDecimal.valueOf(200), results.get(category2));
//
//        verify(expenseOperations, times(2)).calculateTotalExpenses(any());
    }

}