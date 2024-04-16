package pl.sonmiike.reportsservice.category;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sonmiike.reportsservice.cateogry.CategoryEntity;
import pl.sonmiike.reportsservice.cateogry.CategoryEntityRepository;
import pl.sonmiike.reportsservice.cateogry.CategoryEntityService;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CategoryEntityServiceTest {


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
        List<CategoryEntity> categoryEntities = List.of(new CategoryEntity());
        // when
        when(categoryEntityService.getCategories()).thenReturn(categoryEntities);
        List<CategoryEntity> result = categoryEntityService.getCategories();

        // then
        assertEquals(categoryEntities, result);
        assertEquals(categoryEntities.size(), result.size());
    }
}
