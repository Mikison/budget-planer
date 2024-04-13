package pl.sonmiike.reportsservice.income;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeEntityRepository IncomeRepository;

    public Optional<List<IncomeEntity>> getIncomesFromDateInterval(LocalDate startDate, LocalDate endDate, Long userId) {
        return IncomeRepository.findIncomeEntitiesByIncomeDateBetweenAndUserUserId(startDate, endDate, userId);
    }
}
