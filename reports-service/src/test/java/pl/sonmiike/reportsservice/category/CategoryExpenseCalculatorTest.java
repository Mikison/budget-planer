package pl.sonmiike.reportsservice.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import pl.sonmiike.reportsservice.expense.Expense;
import pl.sonmiike.reportsservice.expense.ExpenseOperations;
import pl.sonmiike.reportsservice.user.UserReport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;


class CategoryExpenseCalculatorTest {


    @InjectMocks
    private CategoryExpenseCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new CategoryExpenseCalculator();
    }


    @Test
    public void testCalculateCategoryExpenses() {
        UserReport userReport = new UserReport(1L);
        Category cat1 = new Category(1L, "Food");
        Category cat2 = new Category(2L, "Utilities");
        List<Category> categories = Arrays.asList(cat1, cat2);

        Expense exp2 = new Expense(2L, "Electricity", "Electricity bill", LocalDate.now() ,BigDecimal.valueOf(100), userReport ,cat2);
        Expense exp1 = new Expense(2L, "Groceries", "Shopping", LocalDate.now() ,BigDecimal.valueOf(200), userReport ,cat1);
        List<Expense> expenses = Arrays.asList(exp1, exp2);

        BigDecimal expectedFoodTotal = BigDecimal.valueOf(200);
        BigDecimal expectedUtilitiesTotal = BigDecimal.valueOf(100);

        try (MockedStatic<ExpenseOperations> mockedOperations = mockStatic(ExpenseOperations.class)) {
            mockedOperations.when(() -> ExpenseOperations.calculateTotalExpenses(any())).thenAnswer(i -> {
                List<Expense> exps = i.getArgument(0);
                return exps.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            });

            HashMap<Category, BigDecimal> results = calculator.calculateCategoryExpenses(expenses, categories);

            assertEquals(expectedFoodTotal, results.get(cat1));
            assertEquals(expectedUtilitiesTotal, results.get(cat2));
        }
    }
}