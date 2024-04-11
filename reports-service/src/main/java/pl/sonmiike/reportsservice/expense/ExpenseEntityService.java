package pl.sonmiike.reportsservice.expense;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseEntityService {

    private final ExpenseEntityRepository expenseEntityRepository;

    public List<ExpenseEntity> getExpensesFromDateBetween(LocalDate starDate, LocalDate endDate, Long userId) {
        return expenseEntityRepository.findExpenseEntitiesByDateBetweenAndUserUserId(starDate, endDate, userId);
    }
}
