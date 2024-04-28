package pl.sonmiike.reportsservice.income;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class IncomeService {

    private final pl.sonmiike.reportsservice.income.IncomeRepository IncomeRepository;

    public Optional<List<Income>> getIncomesFromDateInterval(LocalDate startDate, LocalDate endDate, Long userId) {
        return IncomeRepository.findIncomeEntitiesByIncomeDateBetweenAndUserUserId(startDate, endDate, userId);
    }
}