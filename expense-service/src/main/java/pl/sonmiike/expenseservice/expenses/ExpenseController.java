package pl.sonmiike.expenseservice.expenses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.sonmiike.budgetapp.exceptions.custom.IdNotMatchingException;
import pl.sonmiike.budgetapp.security.auth.AuthService;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<PagedExpensesDTO> getUserExpenses(Authentication authentication,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Long userId = authService.getUserId(authentication);
        return ResponseEntity.ok(expenseService.fetchUserExpenses(userId, page, size));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PagedExpensesDTO> getUserExpensesByCategory(Authentication authentication,
                                                                      @PathVariable Long categoryId,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        Long userId = authService.getUserId(authentication);
        return ResponseEntity.ok(expenseService.fetchUserExpensesByCategory(userId, categoryId, page, size));
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long expenseId, Authentication authentication) {
        Long userId = authService.getUserId(authentication);
        return ResponseEntity.ok(expenseService.fetchExpenseById(expenseId, userId));
    }

    @GetMapping("/filter")
    public ResponseEntity<PagedExpensesDTO> getExpenses(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) LocalDate dateTo,
            @RequestParam(value = "fromAmount", required = false) BigDecimal fromAmount,
            @RequestParam(value = "toAmount", required = false) BigDecimal toAmount,
            @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(expenseService.fetchExpensesWithFilters(keyword, dateFrom, dateTo, fromAmount, toAmount, pageable));
    }

    @PostMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createExpense(@RequestBody @Valid AddExpenseDTO expenseDTO, @PathVariable Long categoryId, Authentication authentication) {
        Long userId = authService.getUserId(authentication);
        expenseService.addExpense(expenseDTO, userId, categoryId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long id, @RequestBody @Valid ExpenseDTO expenseDTO, Authentication authentication) {
        if (!id.equals(expenseDTO.getId())) {
            throw new IdNotMatchingException("Id in path and in request body must be the same");
        }
        Long userId = authService.getUserId(authentication);
        ExpenseDTO updatedExpense = expenseService.updateExpense(expenseDTO, userId);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/{expenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable Long expenseId, Authentication authentication) {
        Long userId = authService.getUserId(authentication);
        expenseService.deleteExpense(expenseId, userId);

    }

}
