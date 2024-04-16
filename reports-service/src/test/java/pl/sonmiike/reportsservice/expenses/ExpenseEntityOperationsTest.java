package pl.sonmiike.reportsservice.expenses;

import org.junit.jupiter.api.Test;
import pl.sonmiike.reportsservice.expense.ExpenseEntity;
import pl.sonmiike.reportsservice.expense.ExpenseOperations;
import pl.sonmiike.reportsservice.report.types.DateInterval;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpenseEntityOperationsTest {


    @Test
    void testCalculateTotalExpenses() {
        List<ExpenseEntity> expenses = Arrays.asList(
                new ExpenseEntity(BigDecimal.valueOf(100)),
                new ExpenseEntity(BigDecimal.valueOf(150))
        );
        BigDecimal result = ExpenseOperations.calculateTotalExpenses(expenses);
        assertEquals(BigDecimal.valueOf(250), result);
    }

    @Test
    void testFindMaxExpense() {
        List<ExpenseEntity> expenses = Arrays.asList(
                new ExpenseEntity(BigDecimal.valueOf(100)),
                new ExpenseEntity(BigDecimal.valueOf(150))
        );
        ExpenseEntity result = ExpenseOperations.findMaxExpense(expenses);
        assertEquals(BigDecimal.valueOf(150), result.getAmount());
    }

    @Test
    void testFindMinExpense() {
        List<ExpenseEntity> expenses = Arrays.asList(
                new ExpenseEntity(BigDecimal.valueOf(100)),
                new ExpenseEntity(BigDecimal.valueOf(50))
        );
        ExpenseEntity result = ExpenseOperations.findMinExpense(expenses);
        assertEquals(BigDecimal.valueOf(50), result.getAmount());
    }

    @Test
    void testCalculateAverageDailyExpenses() {
        List<ExpenseEntity> expenses = Arrays.asList(
                new ExpenseEntity(BigDecimal.valueOf(300)),
                new ExpenseEntity(BigDecimal.valueOf(150))
        );
        BigDecimal result = ExpenseOperations.calculateAverageDailyExpenses(expenses, 10);
        assertEquals(BigDecimal.valueOf(45), result);
    }

    @Test
    void testCalculateAverageWeeklyExpenses() {
        List<ExpenseEntity> expenses = Arrays.asList(
                new ExpenseEntity(BigDecimal.valueOf(300)),
                new ExpenseEntity(BigDecimal.valueOf(200))
        );
        BigDecimal result = ExpenseOperations.calculateAverageWeeklyExpenses(expenses);
        assertEquals(BigDecimal.valueOf(125), result);
    }

    @Test
    void testCalculateWeekWithBiggestExpense() {
        LocalDate date1 = LocalDate.of(2024, 4, 20);
        LocalDate date2 = LocalDate.of(2024, 4, 25);
        List<ExpenseEntity> expenses = Arrays.asList(
                new ExpenseEntity(BigDecimal.valueOf(500), date1),
                new ExpenseEntity(BigDecimal.valueOf(200), date2)
        );
        DateInterval result = ExpenseOperations.calculateWeekWithBiggestExpense(expenses);
        assertEquals(date1.with(DayOfWeek.MONDAY), result.getStartDate());
        assertEquals(date1.with(DayOfWeek.MONDAY).plusDays(6), result.getEndDate());
    }

    @Test
    void testGetDayWithHighestAverageExpense() {
        LocalDate date1 = LocalDate.of(2024, 4, 20); // Saturday
        LocalDate date2 = LocalDate.of(2024, 4, 21); // Sunday
        List<ExpenseEntity> expenses = Arrays.asList(
                new ExpenseEntity(BigDecimal.valueOf(300), date1),
                new ExpenseEntity(BigDecimal.valueOf(600), date2)
        );
        DayOfWeek result = ExpenseOperations.getDayWithHighestAverageExpense(expenses);
        assertEquals(DayOfWeek.SUNDAY, result);
    }

    @Test
    void testCalculateTotalExpensesWithEmptyList() {
        List<ExpenseEntity> expenses = List.of();
        BigDecimal result = ExpenseOperations.calculateTotalExpenses(expenses);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testHandlingNullExpenseList() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                ExpenseOperations.calculateTotalExpenses(null)
        );
        assertEquals("Expenses list cannot be null", exception.getMessage());
    }


}
