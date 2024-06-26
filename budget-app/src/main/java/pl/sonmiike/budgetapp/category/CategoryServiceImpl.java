package pl.sonmiike.budgetapp.category;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sonmiike.budgetapp.category.monthlyBudget.MonthlyBudget;
import pl.sonmiike.budgetapp.category.monthlyBudget.MonthlyBudgetDTO;
import pl.sonmiike.budgetapp.category.monthlyBudget.MonthlyBudgetRepository;
import pl.sonmiike.budgetapp.exceptions.custom.ResourceNotFoundException;
import pl.sonmiike.budgetapp.expenses.Expense;
import pl.sonmiike.budgetapp.expenses.ExpenseRepository;
import pl.sonmiike.budgetapp.user.UserEntity;
import pl.sonmiike.budgetapp.user.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final MonthlyBudgetRepository monthlyBudgetRepository;

    private final CategoryMapper categoryMapper;


    public Set<CategoryDTO> fetchAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toSet());
    }

    public Set<CategoryDTO> fetchUserCategories(Long userId) {
        List<Category> categories = categoryRepository.findAllCategoriesByUserId(userId);
        List<MonthlyBudget> budgets = monthlyBudgetRepository.findAllByUserUserId(userId);
        Map<Long, BigDecimal> budgetMap = budgets.stream()
                .collect(Collectors.groupingBy(
                        budget -> budget.getCategory().getId(),
                        Collectors.reducing(
                                BigDecimal.ZERO, MonthlyBudget::getBudgetAmount, BigDecimal::add
                        )
                ));
        return categories.stream()
                .map(category -> {
                    CategoryDTO dto = categoryMapper.toDTO(category);
                    dto.setBudget(budgetMap.getOrDefault(category.getId(), BigDecimal.ZERO));
                    return dto;
                })
                .collect(Collectors.toSet());
    }

    public Category fetchCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with that id not found in database"));
    }

    public Category fetchCategoryByUserIdAndId(Long userId,Long categoryId) {
        UserCategory userCategory = userCategoryRepository.findByUserUserIdAndCategoryId(userId, categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not have this category assigned"));
        return userCategory.getCategory();
    }


    public Category addCategoriyAndAssign(Long userId, AddCategoryDTO categoryDTO) {
        String categoryName = capitalizeFirstLetter(categoryDTO.getName().toLowerCase());
        Category category = categoryRepository.findByNameIgnoreCase(categoryName);

        if (category == null) {
            category = categoryMapper.toEntity(categoryDTO);
            category.setName(categoryName);
            categoryRepository.save(category);
        }

        assignCategoryToUser(userId, category.getId(), categoryDTO.getIconUrl());
        return category;
    }

    public void assignCategoryToUser(Long userId, Long categoryId, String iconUrl) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with that id not found in database"));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found in the database"));

        UserCategory userCategory = UserCategory.builder()
                .user(user)
                .category(category)
                .assignedAt(LocalDateTime.now())
                .iconUrl(iconUrl)
                .build();
        userCategoryRepository.save(userCategory);
    }

    public void removeCategoryFromUser(Long userId, Long categoryId) {
        UserCategory userCategory = userCategoryRepository.findByUserUserIdAndCategoryId(userId, categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not have this category assigned"));

        expenseRepository.deleteAllByCategoryIdAndUserUserId(userId, categoryId); // TODO DELETE THEM OR JUST STORE THEM IN EXPESNES TAB WITH DELETED | NULL | EMPTY CATEGORY

        userCategoryRepository.delete(userCategory);
    }

    public MonthlyBudgetDTO setBudgetAmountForCategory(Long userId, MonthlyBudgetDTO monthlyBudgetDTO) {
        UserCategory userCategory = userCategoryRepository.findByUserUserIdAndCategoryId(userId, monthlyBudgetDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("User does not have this category assigned"));

        YearMonth currentYearMonth = YearMonth.now();
        BigDecimal budgetToSet = monthlyBudgetDTO.getBudgetToSet();

        int updatedRows = monthlyBudgetRepository.updateBudgetAmountByUserIdAndCategoryIdAndYearMonth(userId, monthlyBudgetDTO.getCategoryId(), currentYearMonth.toString(), budgetToSet);

        if (updatedRows == 0) {
            MonthlyBudget newBudget = MonthlyBudget.builder()
                    .yearMonth(currentYearMonth.toString())
                    .budgetAmount(budgetToSet)
                    .spentAmount(expenseRepository.findAll().stream()
                            .map(Expense::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add))
                    .category(userCategory.getCategory())
                    .updatedAt(LocalDateTime.now())
                    .user(userCategory.getUser())
                    .build();
            monthlyBudgetRepository.save(newBudget);
        }

        return MonthlyBudgetDTO.builder()
                .categoryId(monthlyBudgetDTO.getCategoryId())
                .budgetToSet(budgetToSet)
                .build();
    }

    @Transactional
    public void deleteMonthlyBudget(Long userId, Long categoryId) {
        if (!userCategoryRepository.existsByUserUserIdAndCategoryId(userId, categoryId)) {
            throw new ResourceNotFoundException("User does not have this category assigned");
        }
        YearMonth currentYearMonth = YearMonth.now();
        monthlyBudgetRepository.deleteByUserUserIdAndCategoryIdAndYearMonth(userId, categoryId, currentYearMonth.toString());
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

}
