package pl.sonmiike.budgetapp.expenses;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sonmiike.budgetapp.category.Category;
import pl.sonmiike.budgetapp.category.CategoryService;
import pl.sonmiike.budgetapp.category.UserCategoryRepository;
import pl.sonmiike.budgetapp.exceptions.custom.IdNotMatchingException;
import pl.sonmiike.budgetapp.exceptions.custom.ResourceNotFoundException;
import pl.sonmiike.budgetapp.user.UserEntity;
import pl.sonmiike.budgetapp.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserCategoryRepository userCategoryRepository;

    private final UserService userService;
    private final CategoryService categoryService;

    private final ExpenseMapper expenseMapper;

    public PagedExpensesDTO fetchUserExpenses(Long userId, int page, int size) {
        Page<Expense> pagedExpenses = expenseRepository.findExpenseByUserUserId(userId, PageRequest.of(page, size));
        return expenseMapper.toPagedDTO(pagedExpenses);
    }

    public PagedExpensesDTO fetchUserExpensesByCategory(Long userId, Long categoryId, int page, int size) {
        Page<Expense> pagedExpenses = expenseRepository.findExpenseByUserUserIdAndCategoryId(userId, categoryId, PageRequest.of(page, size));
        return expenseMapper.toPagedDTO(pagedExpenses);
    }

    public ExpenseDTO fetchExpenseById(Long id, Long userId) {
        return expenseRepository.findByIdAndUserUserId(id, userId)
                .map(expenseMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with that id not found in database"));

    }

    public void addExpense(AddExpenseDTO expenseDTO, Long userId, Long categoryId) {
        if (!userCategoryRepository.existsByUserUserIdAndCategoryId(userId, categoryId)) {
            throw new IdNotMatchingException("User does not have category with that id assigned");
        }
        Expense expense = expenseMapper.toEntity(expenseDTO);
        expense.setUser(userService.fetchUserById(userId));

        expense.setCategory(categoryService.fetchCategoryById(categoryId));
        expenseRepository.save(expense);
    }

    public PagedExpensesDTO fetchExpensesWithFilters(String keyword, LocalDate dateFrom, LocalDate dateTo, BigDecimal amountFrom, BigDecimal amountTo, Pageable pageable) {
        Page<Expense> pagedFilteredExpenses = expenseRepository.findAll(ExpenseFilterSortingSpecifications.withFilters(keyword, dateFrom, dateTo, amountFrom, amountTo), pageable);
        return expenseMapper.toPagedDTO(pagedFilteredExpenses);
    }

    @Transactional
    public ExpenseDTO updateExpense(ExpenseDTO expenseDTOtoUpdate, Long userId) {
        Expense expense = expenseRepository.findByIdAndUserUserId(expenseDTOtoUpdate.getId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with that id not found in database"));

        Category category = categoryService.fetchCategoryByUserIdAndId(userId, expenseDTOtoUpdate.getCategoryId());

        UserEntity user = userService.fetchUserById(userId);

        expense.update(expenseDTOtoUpdate);
        expense.setUser(user);
        expense.setCategory(category);

        expenseRepository.save(expense);
        return expenseMapper.toDTO(expense);
    }


    @Transactional
    public void deleteExpense(Long expenseId, Long userId) {
        if (!expenseRepository.existsByIdAndUserUserId(expenseId, userId)) {
            throw new ResourceNotFoundException("Expense not found for that user assigned");
        }
        expenseRepository.deleteByIdAndUserUserId(expenseId, userId);
    }

}
