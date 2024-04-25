package pl.sonmiike.reportsservice.category;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.cateogry.Category;
import pl.sonmiike.reportsservice.cateogry.CategoryEntityRepository;
import pl.sonmiike.reportsservice.cateogry.CategoryEntityService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CategoryServiceTest {


    @Mock
    private CategoryEntityRepository categoryEntityRepository;


    @InjectMocks
    private CategoryEntityService categoryEntityService;

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
    void getCategories() {
        // given
        List<Category> categoryEntities = List.of(new Category());
        // when
        when(categoryEntityService.getCategories()).thenReturn(categoryEntities);
        List<Category> result = categoryEntityService.getCategories();

        // then
        assertEquals(categoryEntities, result);
        assertEquals(categoryEntities.size(), result.size());
    }
}
