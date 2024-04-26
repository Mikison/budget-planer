package pl.sonmiike.reportsservice.income;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class IncomeOperations {


    public static BigDecimal getTotalIncomes(@NotNull List<Income> incomes) {
        if (incomes == null) throw new IllegalArgumentException();

        return incomes.stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
