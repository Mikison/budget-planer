package pl.sonmiike.reportsservice.income;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeEntityRepository IncomeRepository;

    public List<IncomeEntity> getIncomesFromDateInterval(LocalDate startDate, LocalDate endDate, Long userId) {
        return IncomeRepository.findIncomeEntitiesByIncomeDateBetweenAndUserUserId(startDate, endDate, userId);
    }
}
