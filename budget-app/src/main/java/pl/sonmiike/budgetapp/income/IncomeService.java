package pl.sonmiike.budgetapp.income;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IncomeService {

    PagedIncomesDTO fetchUserIncome(Long userId, int page, int size);

    IncomeDTO fetchIncomeById(Long id, Long userId);

    void addIncome(AddIncomeDTO incomeDTO, Long userId);

    PagedIncomesDTO fetchIncomesWithFilters(String keyword, LocalDate dateFrom, LocalDate dateTo, BigDecimal amountFrom, BigDecimal amountTo, Pageable pageable);

    IncomeDTO updateIncome(IncomeDTO incomeDTOtoUpdate, Long userId);

    void deleteIncome(Long incomeId, Long userId);
}
