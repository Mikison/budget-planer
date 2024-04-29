package pl.sonmiike.budgetapp.expenses;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExpenseService {

    PagedExpensesDTO fetchUserExpenses(Long userId, int page, int size);

    PagedExpensesDTO fetchUserExpensesByCategory(Long userId, Long categoryId, int page, int size);

    ExpenseDTO fetchExpenseById(Long id, Long userId);

    void addExpense(AddExpenseDTO expenseDTO, Long userId, Long categoryId);

    PagedExpensesDTO fetchExpensesWithFilters(String keyword, LocalDate dateFrom, LocalDate dateTo, BigDecimal amountFrom, BigDecimal amountTo, Pageable pageable);

    ExpenseDTO updateExpense(ExpenseDTO expenseDTOtoUpdate, Long userId);

    void deleteExpense(Long expenseId, Long userId);
}
