package pl.sonmiike.reportsservice.income;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IncomeEntityRepository extends JpaRepository<IncomeEntity, Long> {

    Optional<List<IncomeEntity>> findIncomeEntitiesByIncomeDateBetweenAndUserUserId(LocalDate starDate, LocalDate endDate, Long userId);

}
