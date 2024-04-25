package pl.sonmiike.financewebapi.category;

import pl.sonmiike.financewebapi.category.monthlyBudget.MonthlyBudgetDTO;

import java.util.Set;

public interface CategoryService {
    Set<CategoryDTO> fetchAllCategories();

    Set<CategoryDTO> fetchUserCategories(Long userId);

    Category createAndAssignCategoryToUser(Long userId, AddCategoryDTO categoryDTO);

    void removeCategoryFromUser(Long userId, Long categoryId);

    MonthlyBudgetDTO setBudgetAmountForCategory(Long userId, MonthlyBudgetDTO monthlyBudgetDTO);

}
