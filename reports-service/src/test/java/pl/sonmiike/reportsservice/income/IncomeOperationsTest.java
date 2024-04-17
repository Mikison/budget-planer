package pl.sonmiike.reportsservice.income;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IncomeOperationsTest {


    @Test
    void testCalculateTotalIncomes() {
        List<IncomeEntity> incomes = Arrays.asList(
                new IncomeEntity(BigDecimal.valueOf(100)),
                new IncomeEntity(BigDecimal.valueOf(150))
        );
        BigDecimal result = IncomeOperations.getTotalIncomes(incomes);
        assertEquals(BigDecimal.valueOf(250), result);
    }


}
