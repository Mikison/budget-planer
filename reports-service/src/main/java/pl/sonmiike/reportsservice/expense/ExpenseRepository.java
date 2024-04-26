package pl.sonmiike.reportsservice.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<List<Expense>> findExpenseEntitiesByDateBetweenAndUserUserId(LocalDate starDate, LocalDate endDate, Long userId);
}
