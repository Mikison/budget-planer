package pl.sonmiike.reportsservice.category;

import pl.sonmiike.reportsservice.expense.Expense;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface CategoryCalculator {
    HashMap<Category, BigDecimal> calculateCategoryExpenses(List<Expense> expenses, List<Category> categories);

}
