package pl.sonmiike.reportsservice.expense;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.types.DateInterval;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExpenseOperations {


    public static BigDecimal calculateTotalExpenses(@NotNull List<Expense> expenses) {
        validateExpensesList(expenses);
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Expense findMaxExpense(@NotNull List<Expense> expenses) {
        validateExpensesList(expenses);
        return expenses.stream()
                .max(Comparator.comparing(Expense::getAmount))
                .orElseThrow(() -> new NullPointerException("Cannot find max expense"));
    }

    public static Expense findMinExpense(@NotNull List<Expense> expenses) {
        validateExpensesList(expenses);
        return expenses.stream()
                .min(Comparator.comparing(Expense::getAmount))
                .orElseThrow(() -> new NullPointerException("Cannot find min expense"));
    }

    public static BigDecimal calculateAverageDailyExpenses(@NotNull List<Expense> expenses, int days) {
        validateExpensesList(expenses);
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(days), RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateAverageWeeklyExpenses(@NotNull List<Expense> expenses) {
        validateExpensesList(expenses);
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(4), RoundingMode.HALF_UP);
    }

    public static DateInterval calculateWeekWithBiggestExpense(@NotNull List<Expense> expenses) {
        validateExpensesList(expenses);
        Map<LocalDate, BigDecimal> weeklyTotalExpenses = expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
                        Collectors.mapping(Expense::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        LocalDate startOfWeek = weeklyTotalExpenses.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new NullPointerException("Cannot find week with biggest expense"));

        LocalDate endOfWeek = startOfWeek.plusDays(6);

        return new DateInterval(startOfWeek, endOfWeek);
    }

    public static DayOfWeek getDayWithHighestAverageExpense(@NotNull List<Expense> expenses) {
        validateExpensesList(expenses);
        Map<DayOfWeek, List<BigDecimal>> expensesPriceByDay = expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getDate().getDayOfWeek(),
                        Collectors.mapping(Expense::getAmount, Collectors.toList())
                ));

        return expensesPriceByDay.entrySet().stream()
                .max(Comparator.comparingDouble(entry ->
                        entry.getValue().stream()
                                .mapToDouble(BigDecimal::doubleValue)
                                .average()
                                .orElse(0)
                ))
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new NullPointerException("Cannot find day with highest average expense"));
    }


    private static void validateExpensesList(List<Expense> expenses) {
        if (expenses == null) throw new NullPointerException("Expenses list cannot be null");
    }

}