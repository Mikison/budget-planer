package pl.sonmiike.budgetapp.expenses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PagedExpensesDTO {

    private int page;
    private int totalPages;
    private List<ExpenseDTO> expenses;
}
