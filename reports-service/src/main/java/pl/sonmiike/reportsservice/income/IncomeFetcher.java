package pl.sonmiike.reportsservice.income;

import pl.sonmiike.reportsservice.report.types.DateInterval;

import java.util.List;

public interface IncomeFetcher {
    List<Income> fetchSortedIncomes(DateInterval date, Long userId);

}
