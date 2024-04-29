package pl.sonmiike.budgetapp.category;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.budgetapp.category.monthlyBudget.MonthlyBudget;
import pl.sonmiike.budgetapp.category.monthlyBudget.MonthlyBudgetDTO;
import pl.sonmiike.budgetapp.category.monthlyBudget.MonthlyBudgetRepository;
import pl.sonmiike.budgetapp.exceptions.custom.ResourceNotFoundException;
import pl.sonmiike.budgetapp.expenses.ExpenseRepository;
import pl.sonmiike.budgetapp.user.UserEntity;
import pl.sonmiike.budgetapp.user.UserRepository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserCategoryRepository userCategoryRepository;

    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private MonthlyBudgetRepository monthlyBudgetRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private AutoCloseable openMocks;

    @BeforeEach
    public void init() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void close() throws Exception {
        openMocks.close();
    }

    @Test
    void testFetchAllCategories() {
        // Setup
        List<Category> categories = Arrays.asList(new Category(1L, "Food"), new Category(2L, "Utilities"));
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDTO(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            return new CategoryDTO(category.getId(), category.getName(), BigDecimal.ZERO);
        });

        // Execution
        Set<CategoryDTO> results = categoryService.fetchAllCategories();

        // Assertions
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(categoryRepository).findAll();
        verify(categoryMapper, times(2)).toDTO(any(Category.class));
    }

    @Test
    void testFetchUserCategories() {
        Long userId = 1L;
        List<Category> categories = Arrays.asList(new Category(1L, "Food"), new Category(2L, "Utilities"));
        when(categoryRepository.findAllCategoriesByUserId(userId)).thenReturn(categories);
        when(categoryMapper.toDTO(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            return new CategoryDTO(category.getId(), category.getName(), BigDecimal.ZERO);
        });

        // Execution
        Set<CategoryDTO> results = categoryService.fetchUserCategories(userId);

        assertNotNull(results);
        assertEquals(2, results.size());
        verify(categoryRepository).findAllCategoriesByUserId(userId);
        verify(categoryMapper, times(2)).toDTO(any(Category.class));
    }


    @Test
    void fetchCategoryById_ShouldReturnCategorySuccess() {
        Long categoryId = 1L;
        Category category = Category.builder().id(categoryId).name("Food").build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryService.fetchCategoryById(categoryId);

        assertEquals(category, result);
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    void fetchCategoryById_ShouldThrowResourceNotFoundException() {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.fetchCategoryById(categoryId));
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    void testCreateAndAssignCategoryForNewCategory_ShouldCreateNewCategoryAndAssignToUserSuccess() {
        Long userId = 1L;
        AddCategoryDTO categoryDTO = AddCategoryDTO.builder().name("TestCategory").build();
        Category category = Category.builder().id(1L).name("TestCategory").build();
        UserEntity userEntity = UserEntity.builder().userId(userId).build();

        when(categoryRepository.findByNameIgnoreCase(anyString())).thenReturn(null);
        when(categoryMapper.toEntity(any(AddCategoryDTO.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(userCategoryRepository.save(any(UserCategory.class))).thenReturn(new UserCategory());

        // When
        Category createdCategory = categoryService.addCategoriyAndAssign(userId, categoryDTO);

        assertEquals(category.getName(), createdCategory.getName());
        verify(categoryRepository, times(1)).save(category);
        verify(userCategoryRepository, times(1)).save(any(UserCategory.class));
        assertNotNull(createdCategory);
    }

    @Test
    void testAddCategoriyAndAssignCategoryToUserWithExistingCategory_ShouldAssignUserToExistingRecordInDatabase() {
        // Given
        Long userId = 1L;
        AddCategoryDTO categoryDTO = AddCategoryDTO.builder().name("TestCategory").build();
        Category category = Category.builder().id(1L).name("TestCategory").build();
        UserEntity userEntity = UserEntity.builder().userId(userId).build();

        when(categoryRepository.findByNameIgnoreCase(anyString())).thenReturn(category);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(userCategoryRepository.save(any(UserCategory.class))).thenReturn(new UserCategory());

        // When
        Category createdCategory = categoryService.addCategoriyAndAssign(userId, categoryDTO);

        assertEquals(category.getName(), createdCategory.getName());
        verify(categoryRepository, never()).save(category);
        verify(userCategoryRepository, times(1)).save(any(UserCategory.class));
        assertNotNull(createdCategory);
    }

    @Test
    void testAssignUserToNonExistingCategory_ShouldThrowResourceNotFoundException() {
        Long userId = 1L;
        Long categoryId = 999L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(UserEntity.builder().userId(userId).build()));

        assertThrows(ResourceNotFoundException.class, () -> categoryService.assignCategoryToUser(userId, categoryId, ""));

        verify(userCategoryRepository, never()).save(any(UserCategory.class));
    }

    @Test
    void testAssignCategoryToNonExistingUser_ShouldThrowResourceNotFoundException() {
        Long userId = 999L;
        Long categoryId = 1L;
        Category category = Category.builder().id(categoryId).name("TestCategory").build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        assertThrows(ResourceNotFoundException.class, () -> categoryService.assignCategoryToUser(userId, categoryId, ""));

        verify(userCategoryRepository, never()).save(any(UserCategory.class));
    }


    @Test
    void testUnassignCategoryFromUser_ShouldUnassignUserFromCategory() {
        Long userId = 1L;
        Long categoryId = 1L;
        UserCategory userCategory = UserCategory.builder().id(1L).build();

        when(userCategoryRepository.findByUserUserIdAndCategoryId(userId, categoryId)).thenReturn(Optional.of(userCategory));

        categoryService.removeCategoryFromUser(userId, categoryId);

        assertEquals(userCategoryRepository.count(), 0);
        verify(expenseRepository, times(1)).deleteAllByCategoryIdAndUserUserId(userId, categoryId);
        verify(userCategoryRepository, times(1)).delete(userCategory);
    }

    @Test
    void unassigningCategoryThatIsNotAssigned_ShouldThrowResourceNotFoundException() {
        Long userId = 1L;
        Long categoryId = 1L;

        when(userCategoryRepository.findByUserUserIdAndCategoryId(userId, categoryId)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> categoryService.removeCategoryFromUser(userId, categoryId));

        verify(expenseRepository, never()).deleteAllByCategoryIdAndUserUserId(userId, categoryId);
        verify(userCategoryRepository, never()).delete(any(UserCategory.class));
    }

    @Test
    void setMonthlyBudgetForCategory_ShouldSuccess() {
        Long userId = 1L;
        MonthlyBudgetDTO inputDTO = MonthlyBudgetDTO.builder().budgetToSet(BigDecimal.valueOf(1000)).build();
        UserCategory mockUserCategory = UserCategory.builder().id(1L).build();

        when(userCategoryRepository.findByUserUserIdAndCategoryId(userId, inputDTO.getCategoryId())).thenReturn(Optional.of(mockUserCategory));
        when(monthlyBudgetRepository.updateBudgetAmountByUserIdAndCategoryIdAndYearMonth(eq(userId), eq(inputDTO.getCategoryId()), any(String.class), eq(inputDTO.getBudgetToSet()))).thenReturn(0);
        when(monthlyBudgetRepository.save(any(MonthlyBudget.class))).thenAnswer(i -> i.getArgument(0));

        // Execution
        MonthlyBudgetDTO result = categoryService.setBudgetAmountForCategory(userId, inputDTO);

        // Verification
        verify(monthlyBudgetRepository).save(any(MonthlyBudget.class));
        assertNotNull(result);
        assertEquals(inputDTO.getBudgetToSet(), result.getBudgetToSet());
    }

    @Test
    void setMonthlyBudgetForCategoryUpdate_ShouldUpdateExistingRecord() {
        // Setup
        Long userId = 1L;
        MonthlyBudgetDTO inputDTO = MonthlyBudgetDTO.builder().budgetToSet(BigDecimal.valueOf(1000)).build();
        UserCategory mockUserCategory = UserCategory.builder().id(1L).build();
        when(userCategoryRepository.findByUserUserIdAndCategoryId(userId, inputDTO.getCategoryId())).thenReturn(Optional.of(mockUserCategory));
        when(monthlyBudgetRepository.updateBudgetAmountByUserIdAndCategoryIdAndYearMonth(eq(userId), eq(inputDTO.getCategoryId()), any(String.class), eq(inputDTO.getBudgetToSet()))).thenReturn(1);

        MonthlyBudgetDTO result = categoryService.setBudgetAmountForCategory(userId, inputDTO);

        // Verification
        verify(monthlyBudgetRepository, never()).save(any(MonthlyBudget.class));
        assertNotNull(result);
        assertEquals(inputDTO.getBudgetToSet(), result.getBudgetToSet());
    }

    @Test
    void testSettingBudgetWhenCategoryIsNotAssignedToUserOrDoesntExist() {
        Long userId = 1L;
        MonthlyBudgetDTO inputDTO = MonthlyBudgetDTO.builder().budgetToSet(BigDecimal.valueOf(1000)).build();

        when(userCategoryRepository.findByUserUserIdAndCategoryId(userId, inputDTO.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.setBudgetAmountForCategory(userId, inputDTO));

        verify(monthlyBudgetRepository, never()).save(any(MonthlyBudget.class));
    }

    @Test
    void deleteMonthlyBudgetWhenCategoryAssigned_ShouldSuccess() {
        Long userId = 1L;
        Long categoryId = 1L;
        YearMonth currentYearMonth = YearMonth.now();
        when(userCategoryRepository.existsByUserUserIdAndCategoryId(userId, categoryId)).thenReturn(true);

        categoryService.deleteMonthlyBudget(userId, categoryId);

        verify(monthlyBudgetRepository, times(1)).deleteByUserUserIdAndCategoryIdAndYearMonth(userId, categoryId, currentYearMonth.toString());
    }

    @Test
    void deleteMonthlyBudgetWhenCategoryNotAssigned_ShouldThrowResourceNotFoundException() {
        Long userId = 1L;
        Long categoryId = 1L;
        YearMonth currentYearMonth = YearMonth.now();
        when(userCategoryRepository.existsByUserUserIdAndCategoryId(userId, categoryId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteMonthlyBudget(userId, categoryId));

        verify(monthlyBudgetRepository, never()).deleteByUserUserIdAndCategoryIdAndYearMonth(userId, categoryId, currentYearMonth.toString());
    }

}
