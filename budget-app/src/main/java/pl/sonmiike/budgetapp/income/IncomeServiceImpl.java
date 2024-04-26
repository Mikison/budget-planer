package pl.sonmiike.budgetapp.income;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sonmiike.budgetapp.exceptions.custom.ResourceNotFoundException;
import pl.sonmiike.budgetapp.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final IncomeMapper incomeMapper;
    private final UserService userService;

    public PagedIncomesDTO fetchUserIncome(Long userId, int page, int size) {
        Page<Income> incomes = incomeRepository.findByUserUserId(userId, PageRequest.of(page, size));
        return incomeMapper.toPagedDTO(incomes);
    }

    public IncomeDTO fetchIncomeById(Long id, Long userId) {
        return incomeRepository.findByIdAndUserUserId(id, userId)
                .map(incomeMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Income not found"));
    }

    public void addIncome(AddIncomeDTO incomeDTO, Long userId) {
        Income income = incomeMapper.toEntity(incomeDTO);
        income.setUser(userService.fetchUserById(userId));
        incomeRepository.save(income);
    }

    public PagedIncomesDTO fetchIncomesWithFilters(String keyword, LocalDate dateFrom, LocalDate dateTo, BigDecimal amountFrom, BigDecimal amountTo, Pageable pageable) {
        Page<Income> pagedFilteredIncomes = incomeRepository.findAll(IncomeFilterSortingSpecifications.withFilters(keyword, dateFrom, dateTo, amountFrom, amountTo), pageable);
        return incomeMapper.toPagedDTO(pagedFilteredIncomes);
    }

    @Transactional
    public IncomeDTO updateIncome(IncomeDTO incomeDTOtoUpdate, Long userId) {
        if (!incomeRepository.existsById(incomeDTOtoUpdate.getId())) {
            throw new ResourceNotFoundException("Income not found");
        }
        Income income = incomeMapper.toEntity(incomeDTOtoUpdate);
        income.setUser(userService.fetchUserById(userId));
        return incomeMapper.toDTO(incomeRepository.save(income));
    }

    @Transactional
    public void deleteIncome(Long incomeId, Long userId) {
        incomeRepository.deleteIncomeByIdAndUserUserId(incomeId, userId);
    }
}
