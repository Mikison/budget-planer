package pl.sonmiike.budgetapp.expenses;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import pl.sonmiike.budgetapp.category.Category;
import pl.sonmiike.budgetapp.category.CategoryService;
import pl.sonmiike.budgetapp.category.UserCategory;
import pl.sonmiike.budgetapp.category.UserCategoryRepository;
import pl.sonmiike.budgetapp.exceptions.custom.IdNotMatchingException;
import pl.sonmiike.budgetapp.exceptions.custom.ResourceNotFoundException;
import pl.sonmiike.budgetapp.user.UserEntity;
import pl.sonmiike.budgetapp.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserCategoryRepository userCategoryRepository;

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ExpenseMapper expenseMapper;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @Captor
    private ArgumentCaptor<Expense> expenseCaptor;


    private AutoCloseable openMocks;

    @BeforeEach
    void init() {
        openMocks = MockitoAnnotations.openMocks(this);

    }

    @AfterEach
    void close() throws Exception {
        openMocks.close();
    }


    @Test
    void testFetchUserExpenses() {
        Long userId = 1L;
        int page = 0;
        int size = 10;
        Expense mockExpense = mock(Expense.class);

        Page<Expense> expensePage = new PageImpl<>(Collections.singletonList(mockExpense));

        PagedExpensesDTO mockPagedExpensesDTO = mock(PagedExpensesDTO.class);

        when(expenseRepository.findExpenseByUserUserId(eq(userId), any(PageRequest.class))).thenReturn(expensePage);
        when(expenseMapper.toPagedDTO(any(Page.class))).thenReturn(mockPagedExpensesDTO);

        PagedExpensesDTO result = expenseService.fetchUserExpenses(userId, page, size);
        assertEquals(mockPagedExpensesDTO, result);
        verify(expenseRepository).findExpenseByUserUserId(eq(userId), eq(PageRequest.of(page, size)));
        verify(expenseMapper).toPagedDTO(expensePage);
    }


    @Test
    void testFetchUserExpensesByCategory() {
        Long userId = 1L;
        Long categoryId = 1L;
        int page = 0;
        int size = 10;

        Expense testExpense = new Expense();
        Page<Expense> expensePage = new PageImpl<>(Collections.singletonList(testExpense), PageRequest.of(page, size), 1);

        PagedExpensesDTO expectedDto = mock(PagedExpensesDTO.class);

        when(expenseRepository.findExpenseByUserUserIdAndCategoryId(eq(userId), eq(categoryId), any(PageRequest.class))).thenReturn(expensePage);
        when(expenseMapper.toPagedDTO(any(Page.class))).thenReturn(expectedDto);

        PagedExpensesDTO result = expenseService.fetchUserExpensesByCategory(userId, categoryId, page, size);

        assertEquals(expectedDto, result);
        verify(expenseRepository).findExpenseByUserUserIdAndCategoryId(eq(userId), eq(categoryId), eq(PageRequest.of(page, size)));
        verify(expenseMapper).toPagedDTO(expensePage);
    }

    @Test
    void testFetchExpenseById_Success() {
        Long id = 1L;
        Long userId = 1L;
        Expense testExpense = new Expense();
        ExpenseDTO expectedDto = new ExpenseDTO();

        when(expenseRepository.findByIdAndUserUserId(eq(id), eq(userId))).thenReturn(Optional.of(testExpense));
        when(expenseMapper.toDTO(any(Expense.class))).thenReturn(expectedDto);

        ExpenseDTO result = expenseService.fetchExpenseById(id, userId);

        assertNotNull(result, "The result should not be null.");
        assertEquals(expectedDto, result, "The returned DTO should match the expected DTO.");
        verify(expenseRepository).findByIdAndUserUserId(id, userId);
        verify(expenseMapper).toDTO(testExpense);
    }

    @Test
    void testFetchExpenseById_NotFound_ThrowsException() {
        Long id = 1L;
        Long userId = 1L;

        when(expenseRepository.findByIdAndUserUserId(eq(id), eq(userId))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expenseService.fetchExpenseById(id, userId), "Expected getExpenseById to throw, but it didn't");

        verify(expenseRepository).findByIdAndUserUserId(id, userId);
        verify(expenseMapper, never()).toDTO(any(Expense.class));
    }

    @Test
    void addExpense_SuccessfulCreation() {
        Long userId = 1L, categoryId = 1L;
        AddExpesneDTO expenseDTO = new AddExpesneDTO("apteka", "leki", LocalDate.now(), BigDecimal.valueOf(150));
        Expense expense = new Expense(1L, "apteka", "leki", LocalDate.now(), BigDecimal.valueOf(150), null, null);

        UserCategory userCategory = new UserCategory();
        when(userCategoryRepository.existsByUserUserIdAndCategoryId(userId, categoryId)).thenReturn(true);
        when(expenseMapper.toEntity(expenseDTO)).thenReturn(expense);
        when(userService.fetchUserById(userId)).thenReturn(new UserEntity());
        when(categoryService.fetchCategoryById(categoryId)).thenReturn(new Category());
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);


        expenseService.addExpense(expenseDTO, userId, categoryId);

        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    void addExpense_IdNotMatchingException() {
        Long userId = 1L, categoryId = 1L;
        AddExpesneDTO expenseDTO = new AddExpesneDTO();

        when(userCategoryRepository.findByUserUserIdAndCategoryId(userId, categoryId))
                .thenReturn(Optional.empty());


        assertThrows(IdNotMatchingException.class, () -> expenseService.addExpense(expenseDTO, userId, categoryId));
    }

    @Test
    void fetchExpensesWithFilters_AllFilters() {
        String keyword = "test";
        LocalDate dateFrom = LocalDate.of(2020, 1, 1);
        LocalDate dateTo = LocalDate.of(2020, 12, 31);
        BigDecimal amountFrom = BigDecimal.valueOf(100);
        BigDecimal amountTo = BigDecimal.valueOf(500);
        PageRequest pageable = PageRequest.of(0, 10);

        Expense testExpense = new Expense(); // Assuming a suitable constructor or setters
        PageImpl<Expense> pagedExpenses = new PageImpl<>(Collections.singletonList(testExpense), pageable, 1);
        PagedExpensesDTO expectedDTO = new PagedExpensesDTO(pagedExpenses.getNumber(), pagedExpenses.getTotalPages(), pagedExpenses.getContent().stream().map(expenseMapper::toDTO).toList());

        when(expenseRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(pagedExpenses);
        when(expenseMapper.toPagedDTO(any())).thenReturn(expectedDTO);

        PagedExpensesDTO result = expenseService.fetchExpensesWithFilters(keyword, dateFrom, dateTo, amountFrom, amountTo, pageable);


        assertEquals(expectedDTO, result);
        verify(expenseRepository).findAll(any(Specification.class), eq(pageable));
        verify(expenseMapper).toPagedDTO(pagedExpenses);
    }

    @Test
    void updateExpense_SuccessfulUpdate() {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(1L);
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        Expense expense = new Expense();

        when(expenseRepository.existsById(expenseDTO.getId())).thenReturn(true);
        when(expenseMapper.toEntity(expenseDTO)).thenReturn(expense);
        when(userService.fetchUserById(userId)).thenReturn(user);
        when(userCategoryRepository.existsByUserUserIdAndCategoryId(userId, expenseDTO.getCategoryId())).thenReturn(true);

        expenseService.updateExpense(expenseDTO, userId);

        verify(expenseRepository).save(expenseCaptor.capture());
        Expense savedExpense = expenseCaptor.getValue();
        assertEquals(userId, savedExpense.getUser().getUserId());
    }

    @Test
    void updateExpense_ExpenseNotFound_ThrowsException() {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(1L);
        Long userId = 1L;

        when(expenseRepository.existsById(expenseDTO.getId())).thenReturn(false);

        Exception exception = assertThrows(IdNotMatchingException.class, () -> expenseService.updateExpense(expenseDTO, userId));

        assertEquals("Expense not found", exception.getMessage());
    }

    @Test
    void updateTest_ExpenseNotFound_ThrowsCategoryUnassiggnedToUser() {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(1L);
        Long userId = 1L;
        Long categoryId = 1L;

        when(expenseRepository.existsById(expenseDTO.getId())).thenReturn(true);
        when(userCategoryRepository.existsByUserUserIdAndCategoryId(userId, categoryId)).thenReturn(false);

        Exception exception = assertThrows(IdNotMatchingException.class, () -> expenseService.updateExpense(expenseDTO, userId));

        assertEquals("User does not have category with that id assigned", exception.getMessage());
    }


    @Test
    void deleteExpense_VerifyRepositoryInteraction() {
        Long expenseId = 1L;
        Long userId = 1L;
        when(expenseRepository.existsByIdAndUserUserId(expenseId, userId)).thenReturn(true);
        expenseService.deleteExpense(expenseId, userId);

        verify(expenseRepository).deleteByIdAndUserUserId(expenseId, userId);
    }

    @Test
    void deleteExpense_ExpenseNotFound_ThrowsException() {
        Long expenseId = 1L;
        Long userId = 1L;
        when(expenseRepository.existsByIdAndUserUserId(expenseId, userId)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> expenseService.deleteExpense(expenseId, userId));

        assertEquals("Expense not found for that user assigned", exception.getMessage());
    }


}
