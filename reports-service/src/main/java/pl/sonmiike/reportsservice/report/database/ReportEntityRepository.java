package pl.sonmiike.reportsservice.report.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReportEntityRepository extends JpaRepository<ReportEntity, Long> {

    boolean existsByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

    List<ReportEntity> findAllByUserUserId(Long id);
}
