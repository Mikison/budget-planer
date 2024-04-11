package pl.sonmiike.reportsservice.income;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class IncomeOperations {


    public static BigDecimal getTotalIncomes(@NotNull List<IncomeEntity> incomes) {
        if (incomes == null) throw new IllegalArgumentException();

        return incomes.stream()
                .map(IncomeEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
