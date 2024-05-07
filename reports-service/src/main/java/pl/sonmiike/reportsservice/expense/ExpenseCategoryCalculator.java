package pl.sonmiike.reportsservice.expense;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sonmiike.reportsservice.category.Category;
import pl.sonmiike.reportsservice.category.CategoryCalculator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExpenseCategoryCalculator implements CategoryCalculator {


    @Override
    public HashMap<Category, BigDecimal> calculateCategoryExpenses(List<Expense> expenses, List<Category> categories) {
        Set<Long> categoryIds = expenses.stream()
                .map(Expense::getCategory)
                .map(Category::getId)
                .collect(Collectors.toSet());
        List<Category> userCategories = categories.stream()
                .filter(category -> categoryIds.contains(category.getId()))
                .toList();

        HashMap<Category, BigDecimal> categoryExpenses = new HashMap<>();
        for (Category category : userCategories) {
            BigDecimal categoryExpense = ExpenseOperations.calculateTotalExpenses(expenses.stream()
                    .filter(expense -> expense.getCategory().getId().equals(category.getId()))
                    .collect(Collectors.toList()));
            categoryExpenses.put(category, categoryExpense);
        }
        return categoryExpenses;    }
}
