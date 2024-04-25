package pl.sonmiike.financewebapi.category;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.financewebapi.category.monthlyBudget.MonthlyBudget;
import pl.sonmiike.financewebapi.category.monthlyBudget.MonthlyBudgetDTO;
import pl.sonmiike.financewebapi.category.monthlyBudget.MonthlyBudgetRepository;
import pl.sonmiike.financewebapi.exceptions.custom.ResourceNotFoundException;
import pl.sonmiike.financewebapi.expenses.ExpenseRepository;
import pl.sonmiike.financewebapi.user.UserEntity;
import pl.sonmiike.financewebapi.user.UserRepository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Optional;

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
    private CategoryServiceImpl categoryServiceImpl;

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
    void getCategoryById_ShouldReturnCategorySuccess() {
        Long categoryId = 1L;
        Category category = Category.builder().id(categoryId).name("Food").build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryServiceImpl.getCategoryById(categoryId);

        assertEquals(category, result);
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    void getCategoryById_ShouldThrowResourceNotFoundException() {
        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.getCategoryById(categoryId));
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
        Category createdCategory = categoryServiceImpl.createAndAssignCategoryToUser(userId, categoryDTO);

        assertEquals(category.getName(), createdCategory.getName());
        verify(categoryRepository, times(1)).save(category);
        verify(userCategoryRepository, times(1)).save(any(UserCategory.class));
        assertNotNull(createdCategory);
    }

    @Test
    void testCreateAndAssignCategoryToUserWithExistingCategory_ShouldAssignUserToExistingRecordInDatabase() {
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
        Category createdCategory = categoryServiceImpl.createAndAssignCategoryToUser(userId, categoryDTO);

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

        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.assignCategoryToUser(userId, categoryId, ""));

        verify(userCategoryRepository, never()).save(any(UserCategory.class));
    }

    @Test
    void testAssignCategoryToNonExistingUser_ShouldThrowResourceNotFoundException() {
        Long userId = 999L;
        Long categoryId = 1L;
        Category category = Category.builder().id(categoryId).name("TestCategory").build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.assignCategoryToUser(userId, categoryId, ""));

        verify(userCategoryRepository, never()).save(any(UserCategory.class));
    }


    @Test
    void testUnassignCategoryFromUser_ShouldUnassignUserFromCategory() {
        Long userId = 1L;
        Long categoryId = 1L;
        UserCategory userCategory = UserCategory.builder().id(1L).build();

        when(userCategoryRepository.findByUserUserIdAndCategoryId(userId, categoryId)).thenReturn(Optional.of(userCategory));

        categoryServiceImpl.removeCategoryFromUser(userId, categoryId);

        assertEquals(userCategoryRepository.count(), 0);
        verify(expenseRepository, times(1)).deleteAllByCategoryIdAndUserUserId(userId, categoryId);
        verify(userCategoryRepository, times(1)).delete(userCategory);
    }

    @Test
    void unassigningCategoryThatIsNotAssigned_ShouldThrowResourceNotFoundException() {
        Long userId = 1L;
        Long categoryId = 1L;

        when(userCategoryRepository.findByUserUserIdAndCategoryId(userId, categoryId)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.removeCategoryFromUser(userId, categoryId));

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
        MonthlyBudgetDTO result = categoryServiceImpl.setBudgetAmountForCategory(userId, inputDTO);

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

        MonthlyBudgetDTO result = categoryServiceImpl.setBudgetAmountForCategory(userId, inputDTO);

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

        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.setBudgetAmountForCategory(userId, inputDTO));

        verify(monthlyBudgetRepository, never()).save(any(MonthlyBudget.class));
    }

    @Test
    void deleteMonthlyBudgetWhenCategoryAssigned_ShouldSuccess() {
        Long userId = 1L;
        Long categoryId = 1L;
        YearMonth currentYearMonth = YearMonth.now();
        when(userCategoryRepository.existsByUserUserIdAndCategoryId(userId, categoryId)).thenReturn(true);

        categoryServiceImpl.deleteMonthlyBudget(userId, categoryId);

        verify(monthlyBudgetRepository, times(1)).deleteByUserUserIdAndCategoryIdAndYearMonth(userId, categoryId, currentYearMonth.toString());
    }

    @Test
    void deleteMonthlyBudgetWhenCategoryNotAssigned_ShouldThrowResourceNotFoundException() {
        Long userId = 1L;
        Long categoryId = 1L;
        YearMonth currentYearMonth = YearMonth.now();
        when(userCategoryRepository.existsByUserUserIdAndCategoryId(userId, categoryId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.deleteMonthlyBudget(userId, categoryId));

        verify(monthlyBudgetRepository, never()).deleteByUserUserIdAndCategoryIdAndYearMonth(userId, categoryId, currentYearMonth.toString());
    }

}
