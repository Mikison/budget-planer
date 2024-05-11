package pl.sonmiike.reportsservice.income;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
class IncomeDataFetcherTest {

    @Mock
    private IncomeService incomeService;

    @InjectMocks
    private IncomeDataFetcher incomeDataFetcher;

    @Test
    void test_fetchSortedIncomes() {
        LocalDate startDate = LocalDate.of(2022, 1, 10);
        LocalDate endDate = LocalDate.of(2022, 1, 20);
        Long userId = 1L;
        DateInterval interval = new DateInterval(startDate, endDate);

        Income income1 = new Income(1L, LocalDate.now().plusDays(2), "Salary", "March Salary", BigDecimal.valueOf(4000), null);
        Income income2 = new Income(1L, LocalDate.now().plusDays(4), "Husband Salary", "March Salary", BigDecimal.valueOf(21903), null);

        List<Income> incomes = Arrays.asList(income1, income2);

        when(incomeService.getIncomesFromDateInterval(startDate, endDate, userId))
                .thenReturn(Optional.of(incomes));

        List<Income> sortedIncomes = incomeDataFetcher.fetchSortedIncomes(interval, userId);

        assertEquals(2, sortedIncomes.size(), "Should return two expenses");
        assertEquals(income1, sortedIncomes.get(0), "Should sort by date correctly");
        assertEquals(income2, sortedIncomes.get(1), "Should sort by date correctly");

        verify(incomeService).getIncomesFromDateInterval(startDate, endDate, userId);
    }

}
