package pl.sonmiike.categoryservice.category;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.sonmiike.categoryservice.AuthService;
import pl.sonmiike.categoryservice.category.monthlyBudget.MonthlyBudgetDTO;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthService authService;


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.fetchAllCategories());
    }

    @GetMapping
    public ResponseEntity<Set<CategoryDTO>> getUserCategories(Authentication authentication) {
        Long userId = authService.getUserId(authentication);
        return ResponseEntity.ok(categoryService.fetchUserCategories(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@RequestBody @Valid AddCategoryDTO categoryDTO, Authentication authentication) {
        Long userId = authService.getUserId(authentication);
        categoryService.addCategoriyAndAssign(userId, categoryDTO);
    }


    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unassignCategory(@PathVariable Long categoryId, Authentication authentication) {
        Long userId = authService.getUserId(authentication);
        categoryService.removeCategoryFromUser(userId, categoryId);
    }

    @PostMapping("/budget")
    public ResponseEntity<MonthlyBudgetDTO> setBudgetForSpecificCategory(@RequestBody @Valid MonthlyBudgetDTO monthlyBudgetDTO, Authentication authentication) {
        Long userId = authService.getUserId(authentication);
        return ResponseEntity.ok(categoryService.setBudgetAmountForCategory(userId, monthlyBudgetDTO));
    }

    @DeleteMapping("/budget/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBudgetForSpecificCategory(@PathVariable Long categoryId, Authentication authentication) {
        Long userId = authService.getUserId(authentication);
        categoryService.deleteMonthlyBudget(userId, categoryId);
    }


}
