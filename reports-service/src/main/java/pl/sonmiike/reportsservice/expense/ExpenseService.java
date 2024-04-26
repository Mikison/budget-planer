package pl.sonmiike.reportsservice.expense;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public Optional<List<Expense>> getExpensesFromDateBetween(LocalDate starDate, LocalDate endDate, Long userId) {
        return expenseRepository.findExpenseEntitiesByDateBetweenAndUserUserId(starDate, endDate, userId);
    }


}
