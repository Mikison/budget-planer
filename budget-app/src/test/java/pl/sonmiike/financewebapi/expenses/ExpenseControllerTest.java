package pl.sonmiike.financewebapi.expenses;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.sonmiike.financewebapi.category.UserCategoryRepository;
import pl.sonmiike.financewebapi.exceptions.custom.ResourceNotFoundException;
import pl.sonmiike.financewebapi.security.auth.AuthService;
import pl.sonmiike.financewebapi.security.auth.JwtService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserCategoryRepository userCategoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ExpenseDTO expenseDTO;
    private PagedExpensesDTO pagedExpensesDTO;

    @MockBean
    private ExpenseRepository expenseRepository;

    @BeforeEach
    void setUp() {
        expenseDTO = ExpenseDTO.builder().id(1L).name("Groceries").description("Today").date(LocalDate.now().toString()).categoryId(1L).amount(BigDecimal.valueOf(100).toString()).build();
        pagedExpensesDTO = PagedExpensesDTO.builder().page(0).totalPages(1).expenses(List.of(expenseDTO)).build();
    }

    @Test
    void getUserExpenses_ReturnsExpenses() throws Exception {
        Long userId = 1L;
        when(authService.getUserId(any())).thenReturn(userId);
        when(expenseService.fetchUserExpenses(userId, 0, 10)).thenReturn(pagedExpensesDTO);

        mockMvc.perform(get("/me/expenses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserExpensesByCategory_ReturnsExpenses() throws Exception {
        Long userId = 1L;
        Long categoryId = 2L;
        when(authService.getUserId(any())).thenReturn(userId);
        when(expenseService.fetchUserExpensesByCategory(userId, categoryId, 0, 10)).thenReturn(pagedExpensesDTO);

        mockMvc.perform(get("/me/expenses/category/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getExpenseById_ReturnsExpense() throws Exception {
        Long userId = 1L;
        Long expenseId = 3L;
        when(authService.getUserId(any())).thenReturn(userId);
        when(expenseService.fetchExpenseById(expenseId, userId)).thenReturn(expenseDTO);

        mockMvc.perform(get("/me/expenses/{expenseId}", expenseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getExpenses_WithFilters_ReturnsFilteredExpenses() throws Exception {
        Long userId = 1L;
        when(authService.getUserId(any())).thenReturn(userId);

        when(expenseService.fetchExpensesWithFilters(anyString(), any(), any(), any(), any(), any())).thenReturn(pagedExpensesDTO);

        mockMvc.perform(get("/me/expenses/filter")
                        .param("keyword", "food")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createExpense_CreatesExpense() throws Exception {
        Long userId = 1L;
        Long categoryId = 2L;
        AddExpesneDTO addExpesneDTO = new AddExpesneDTO("Groceries", "Walmart", LocalDate.now(), BigDecimal.valueOf(100));
        when(authService.getUserId(Mockito.any())).thenReturn(userId);
        Mockito.doNothing().when(expenseService).addExpense(addExpesneDTO, userId, categoryId);

        mockMvc.perform(post("/me/expenses/{categoryId}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addExpesneDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteExpense_DeletesExpense() throws Exception {
        Long userId = 1L;
        Long expenseId = 3L;
        expenseDTO.setUserId(userId);
        when(authService.getUserId(Mockito.any())).thenReturn(userId);
        when(expenseService.fetchExpenseById(expenseId, userId)).thenReturn(expenseDTO);
        Mockito.doNothing().when(expenseService).deleteExpense(expenseId, userId);

        mockMvc.perform(delete("/me/expenses/{expenseId}", expenseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteExpense_NotFound() throws Exception {
        Long expenseId = 1L;
        Long userId = 1L;
        doThrow(new ResourceNotFoundException("Expense not found for that user assigned"))
                .when(expenseService).deleteExpense(expenseId, userId);
        when(authService.getUserId(any())).thenReturn(userId);

        mockMvc.perform(delete("/me/expenses/{expenseId}", expenseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(expenseService).deleteExpense(expenseId, userId);
        verify(authService).getUserId(any());
    }

    @Test
    public void updateExpense_Success() throws Exception {
        Long id = 1L;
        ExpenseDTO expenseDTO = new ExpenseDTO(id, "Coffee", "Morning coffee", "2023-04-18", "2.50", null, 1L);
        ExpenseDTO updatedExpenseDTO = new ExpenseDTO(id, "Coffee", "Morning coffee", "2023-04-18", "2.50", 1L, 1L);
        when(authService.getUserId(any())).thenReturn(1L);
        when(expenseService.updateExpense(any(ExpenseDTO.class), anyLong())).thenReturn(updatedExpenseDTO);
        mockMvc.perform(put("/me/expenses/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedExpenseDTO)));

        verify(authService, times(1)).getUserId(any());
        verify(expenseService, times(1)).updateExpense(any(ExpenseDTO.class), eq(1L));
    }

    @Test
    void updateExpense_ThrowsIdNotMatchingException() throws Exception {
        Long id = 1L;
        ExpenseDTO expenseDTO = new ExpenseDTO(id, "Coffee", "Morning coffee", "2023-04-18", "2.50", null, 1L);
        ExpenseDTO updatedExpenseDTO = new ExpenseDTO(id, "Coffee", "Morning coffee", "2023-04-18", "2.50", 1L, 1L);
        when(authService.getUserId(any())).thenReturn(1L);
        when(expenseService.updateExpense(any(ExpenseDTO.class), anyLong())).thenReturn(updatedExpenseDTO);
        mockMvc.perform(put("/me/expenses/{id}", id + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expenseDTO)))
                .andExpect(status().isConflict());

        verify(authService, never()).getUserId(any());
        verify(expenseService, never()).updateExpense(any(ExpenseDTO.class), eq(1L));
    }


}
