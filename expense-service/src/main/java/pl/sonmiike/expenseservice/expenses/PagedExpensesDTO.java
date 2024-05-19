package pl.sonmiike.expenseservice.expenses;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PagedExpensesDTO {

    private int page;
    private int totalPages;
    private List<ExpenseDTO> expenses;
}
