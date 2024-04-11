package pl.sonmiike.reportsservice.income;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IncomeEntityRepository extends JpaRepository<IncomeEntity, Long> {

    List<IncomeEntity> findIncomeEntitiesByIncomeDateBetweenAndUserUserId(LocalDate starDate, LocalDate endDate, Long userId);

}
