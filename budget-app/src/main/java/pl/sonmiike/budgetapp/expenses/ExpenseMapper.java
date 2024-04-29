package pl.sonmiike.budgetapp.expenses;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    public ExpenseDTO toDTO(Expense expense) {
        return ExpenseDTO.builder()
                .id(expense.getId())
                .name(expense.getName())
                .description(expense.getDescription())
                .date(expense.getDate())
                .amount(expense.getAmount())
                .userId(expense.getUser().getUserId())
                .categoryId(expense.getCategory().getId())
                .build();
    }

    public Expense toEntity(ExpenseDTO expenseDTO) {
        return Expense.builder()
                .id(expenseDTO.getId())
                .name(expenseDTO.getName())
                .description(expenseDTO.getDescription())
                .date((expenseDTO.getDate()))
                .amount(expenseDTO.getAmount())
                .build();
    }

    public Expense toEntity(AddExpenseDTO expenseDTO) {
        return Expense.builder()
                .name(expenseDTO.getName())
                .description(expenseDTO.getDescription())
                .date(expenseDTO.getDate())
                .amount(expenseDTO.getAmount())
                .build();
    }


    public PagedExpensesDTO toPagedDTO(Page<Expense> expenses) {
        return PagedExpensesDTO.builder()
                .page(expenses.getNumber())
                .totalPages(expenses.getTotalPages())
                .expenses(expenses.getContent().stream().map(this::toDTO).toList())
                .build();
    }
}
