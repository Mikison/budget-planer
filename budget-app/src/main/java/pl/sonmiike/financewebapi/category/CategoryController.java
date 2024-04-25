package pl.sonmiike.financewebapi.category;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.sonmiike.financewebapi.category.monthlyBudget.MonthlyBudgetDTO;
import pl.sonmiike.financewebapi.security.auth.AuthService;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me/category")
public class CategoryController {

    private final CategoryServiceImpl categoryServiceImpl;
    private final AuthService authService;


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryServiceImpl.fetchAllCategories());
    }

    @GetMapping
    public ResponseEntity<Set<CategoryDTO>> getUserCategories(Authentication authentication) {
        Long userId = authService.getUserId(authentication);
        return ResponseEntity.ok(categoryServiceImpl.fetchUserCategories(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@RequestBody @Valid AddCategoryDTO categoryDTO, Authentication authentication) {
        Long userId = authService.getUserId(authentication);
        categoryServiceImpl.createAndAssignCategoryToUser(userId, categoryDTO);
    }


    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unassignCategory(@PathVariable Long categoryId, Authentication authentication) {
        Long userId = authService.getUserId(authentication);
        categoryServiceImpl.removeCategoryFromUser(userId, categoryId);
    }

    @PostMapping("/budget")
    public ResponseEntity<MonthlyBudgetDTO> setBudgetForSpecificCategory(@RequestBody @Valid MonthlyBudgetDTO monthlyBudgetDTO, Authentication authentication) {
        Long userId = authService.getUserId(authentication);
        return ResponseEntity.ok(categoryServiceImpl.setBudgetAmountForCategory(userId, monthlyBudgetDTO));
    }

    @DeleteMapping("/budget/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBudgetForSpecificCategory(@PathVariable Long categoryId, Authentication authentication) {
        Long userId = authService.getUserId(authentication);
        categoryServiceImpl.deleteMonthlyBudget(userId, categoryId);
    }


}
