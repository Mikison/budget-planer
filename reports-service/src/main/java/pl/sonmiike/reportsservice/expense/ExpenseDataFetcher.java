package pl.sonmiike.reportsservice.expense;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.report.types.DateInterval;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseDataFetcher implements ExpenseFetcher {

    private final ExpenseService expenseService;


    @Override
    public List<Expense> fetchSortedExpenses(DateInterval date, Long userId) {
        return expenseService.getExpensesFromDateBetween(date.getStartDate(), date.getEndDate(), userId)
                .orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(Expense::getDate))
                .toList();
    }
}
