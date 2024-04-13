package pl.sonmiike.reportsservice.expense;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseEntityService {

    private final ExpenseEntityRepository expenseEntityRepository;

    public Optional<List<ExpenseEntity>> getExpensesFromDateBetween(LocalDate starDate, LocalDate endDate, Long userId) {
        return expenseEntityRepository.findExpenseEntitiesByDateBetweenAndUserUserId(starDate, endDate, userId);
    }
}
