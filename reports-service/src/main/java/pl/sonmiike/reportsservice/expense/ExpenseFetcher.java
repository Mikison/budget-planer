package pl.sonmiike.reportsservice.expense;

import pl.sonmiike.reportsservice.report.types.DateInterval;

import java.util.List;

public interface ExpenseFetcher {
    List<Expense> fetchSortedExpenses(DateInterval date, Long userId);

}
