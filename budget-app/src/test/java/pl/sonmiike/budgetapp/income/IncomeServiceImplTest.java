package pl.sonmiike.budgetapp.income;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.sonmiike.budgetapp.exceptions.custom.ResourceNotFoundException;
import pl.sonmiike.budgetapp.user.UserEntity;
import pl.sonmiike.budgetapp.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class IncomeServiceImplTest {


    @Mock
    private IncomeRepository incomeRepository;
    @Mock
    private IncomeMapper incomeMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private IncomeServiceImpl incomeService;

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
    void testFetchUserIncome() {
        Long userId = 1L;
        int page = 0;
        int size = 10;

        List<Income> incomeList = new ArrayList<>();
        incomeList.add(Income.builder()
                .id(1L)
                .incomeDate(LocalDate.now())
                .name("Salary")
                .description("Monthly Salary")
                .amount(BigDecimal.valueOf(5000))
                .user(new UserEntity())
                .build());

        Page<Income> incomePage = new PageImpl<>(incomeList);

        when(incomeRepository.findByUserUserId(userId, PageRequest.of(page, size))).thenReturn(incomePage);

        List<IncomeDTO> incomeDTOList = new ArrayList<>();
        incomeDTOList.add(IncomeDTO.builder()
                .id(1L)
                .incomeDate(LocalDate.now())
                .name("Salary")
                .description("Monthly Salary")
                .amount(BigDecimal.valueOf(5000))
                .build());

        PagedIncomesDTO expectedPagedIncomesDTO = PagedIncomesDTO.builder()
                .currentPage(0)
                .totalPages(1)
                .incomes(incomeDTOList)
                .build();

        when(incomeMapper.toPagedDTO(incomePage)).thenReturn(expectedPagedIncomesDTO);

        PagedIncomesDTO result = incomeService.fetchUserIncome(userId, page, size);

        assertNotNull(result);
        assertEquals(expectedPagedIncomesDTO, result);
        verify(incomeRepository, times(1)).findByUserUserId(userId, PageRequest.of(page, size));
        verify(incomeMapper, times(1)).toPagedDTO(incomePage);
    }

    @Test
    void fetchIncomeById_ShouldReturnIncomeDTO() {
        Long incomeId = 1L, userId = 1L;
        Income income = new Income(incomeId, LocalDate.now(), "Test", "beka", BigDecimal.valueOf(100.00), null);
        IncomeDTO incomeDTO = new IncomeDTO(incomeId, LocalDate.now(), "Test", "beka", BigDecimal.valueOf(100.00), userId);
        when(incomeRepository.findByIdAndUserUserId(eq(incomeId), eq(userId))).thenReturn(Optional.of(income));
        when(incomeMapper.toDTO(eq(income))).thenReturn(incomeDTO);

        IncomeDTO result = incomeService.fetchIncomeById(incomeId, userId);

        assertEquals(incomeDTO, result);
        assertNotNull(result);
        verify(incomeRepository).findByIdAndUserUserId(eq(incomeId), eq(userId));
        verify(incomeMapper).toDTO(eq(income));
    }

    @Test
    void fetchIncomeById_ShouldThrowResourceNotFoundException() {
        Long incomeId = 1L, userId = 1L;

        when(incomeRepository.findByIdAndUserUserId(eq(incomeId), eq(userId))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> incomeService.fetchIncomeById(incomeId, userId));

        verify(incomeRepository).findByIdAndUserUserId(incomeId, userId);
        verify(incomeMapper, never()).toDTO(any(Income.class));
    }

    @Test
    void addIncome_SuccessfulCreation() {
        Long userId = 1L;
        AddIncomeDTO addIncomeDTO = new AddIncomeDTO(LocalDate.now(), "Salary", "May Salary", BigDecimal.valueOf(150.00));
        Income income = new Income(1L, LocalDate.now(), "Salary", "May Salary", BigDecimal.valueOf(150.00), UserEntity.builder().userId(userId).build());

        when(incomeMapper.toEntity(eq(addIncomeDTO))).thenReturn(income);
        when(userService.fetchUserById(eq(userId))).thenReturn(UserEntity.builder().userId(userId).build());

        incomeService.addIncome(addIncomeDTO, userId);

        verify(incomeMapper, times(1)).toEntity(eq(addIncomeDTO));
        verify(userService, times(1)).fetchUserById(eq(userId));
        verify(incomeRepository, times(1)).save(eq(income));


    }

    @Test
    public void testUpdateIncome_Success() {
        // Arrange
        Long userId = 1L;
        Long incomeId = 1L;
        IncomeDTO incomeDTOtoUpdate = new IncomeDTO();
        incomeDTOtoUpdate.setId(incomeId);

        Income income = new Income();
        UserEntity user = new UserEntity();
        user.setUserId(userId);

        when(incomeRepository.findById(incomeId)).thenReturn(Optional.of(income));
        when(userService.fetchUserById(userId)).thenReturn(user);
        when(incomeMapper.toDTO(any(Income.class))).thenReturn(incomeDTOtoUpdate);

        // Act
        IncomeDTO result = incomeService.updateIncome(incomeDTOtoUpdate, userId);

        // Assert
        assertNotNull(result);
        assertEquals(incomeId, result.getId());
        verify(incomeRepository).save(income);
    }


    @Test
    public void testUpdateIncome_Failure_IncomeNotFound() {
        Long userId = 1L;
        Long incomeId = 1L;
        IncomeDTO incomeDTOtoUpdate = new IncomeDTO();
        incomeDTOtoUpdate.setId(incomeId);

        when(incomeRepository.findById(incomeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> incomeService.updateIncome(incomeDTOtoUpdate, userId));

        assertEquals("Income not found", exception.getMessage());
        verify(incomeRepository, never()).save(any(Income.class));
    }


    @Test
    void testDeleteIncome() {
        Long incomeId = 1L, userId = 1L;

        incomeService.deleteIncome(incomeId, userId);

        verify(incomeRepository, times(1)).deleteIncomeByIdAndUserUserId(eq(incomeId), eq(userId));
        assertEquals(incomeRepository.count(), 0);
    }

    @Test
    void testFetchIncomesWithFilters() {
        String keyword = "test";
        LocalDate dateFrom = LocalDate.of(2022, 1, 1);
        LocalDate dateTo = LocalDate.of(2022, 12, 31);
        BigDecimal amountFrom = new BigDecimal("100.00");
        BigDecimal amountTo = new BigDecimal("1000.00");
        Pageable pageable = Pageable.unpaged();

        Page<Income> mockPage = mock(Page.class);
        PagedIncomesDTO mockPagedIncomesDTO = new PagedIncomesDTO(mockPage.getNumber(), mockPage.getTotalPages(), mockPage.getContent().stream().map(incomeMapper::toDTO).toList());
        mockPagedIncomesDTO.setIncomes(Collections.emptyList()); // Assume an empty list for simplicity

        when(incomeRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(mockPage);
        when(incomeMapper.toPagedDTO(mockPage)).thenReturn(mockPagedIncomesDTO);

        PagedIncomesDTO result = incomeService.fetchIncomesWithFilters(keyword, dateFrom, dateTo, amountFrom, amountTo, pageable);

        assertEquals(mockPagedIncomesDTO, result);
        verify(incomeRepository, times(1)).findAll(any(Specification.class), eq(pageable));
        verify(incomeMapper, times(1)).toPagedDTO(mockPage);
    }
}
