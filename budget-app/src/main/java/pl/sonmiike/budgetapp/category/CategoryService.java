package pl.sonmiike.budgetapp.category;

import pl.sonmiike.budgetapp.category.monthlyBudget.MonthlyBudgetDTO;

import java.util.Set;

public interface CategoryService {

    Set<CategoryDTO> fetchAllCategories();

    Set<CategoryDTO> fetchUserCategories(Long userId);

    Category fetchCategoryById(Long id);

    Category createAndAssignCategoryToUser(Long userId, AddCategoryDTO categoryDTO);

    void assignCategoryToUser(Long userId, Long categoryId, String iconUrl);

    void removeCategoryFromUser(Long userId, Long categoryId);

    MonthlyBudgetDTO setBudgetAmountForCategory(Long userId, MonthlyBudgetDTO monthlyBudgetDTO);

    void deleteMonthlyBudget(Long userId, Long categoryId);
}
