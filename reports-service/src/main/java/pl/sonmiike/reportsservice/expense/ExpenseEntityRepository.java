package pl.sonmiike.reportsservice.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseEntityRepository extends JpaRepository<ExpenseEntity, Long> {
    Optional<List<ExpenseEntity>> findExpenseEntitiesByDateBetweenAndUserUserId(LocalDate starDate, LocalDate endDate, Long userId);

    Optional<List<ExpenseEntity>> findExpenseEntitiesByDateBetweenAndUserUserIdAndCategory_Id(LocalDate starDate, LocalDate endDate, Long userId, Long categoryId);}
