package pl.sonmiike.reportsservice.income;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.types.DateInterval;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IncomeDataFetcher implements IncomeFetcher {

    private final IncomeService incomeService;

    @Override
    public List<Income> fetchSortedIncomes(DateInterval date, Long userId) {
        return incomeService.getIncomesFromDateInterval(date.getStartDate(), date.getEndDate(), userId)
                .orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(Income::getIncomeDate))
                .toList();
    }
}
