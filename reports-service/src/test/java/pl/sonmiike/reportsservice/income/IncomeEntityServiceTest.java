package pl.sonmiike.reportsservice.income;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IncomeEntityServiceTest {

    @Mock
    private IncomeEntityRepository incomeRepository;

    @InjectMocks
    private IncomeEntityService incomeEntityService;

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
    void getIncomesFromDateInterval() {
        // given
        List<IncomeEntity> incomeEntities = List.of(new IncomeEntity());
        LocalDate startDate = LocalDate.of(2024,4,1);
        LocalDate endDate = LocalDate.of(2024,4,7);
        Long userId = 1L;
        // when
        when(incomeEntityService.getIncomesFromDateInterval(eq(startDate), eq(endDate), anyLong())).thenReturn(Optional.of(incomeEntities));

        Optional<List<IncomeEntity>> result = incomeEntityService.getIncomesFromDateInterval(startDate, endDate, userId);

        // then
        assertNotNull(result);
        assertTrue(result.isPresent(), "Income list should be present");
        assertEquals(incomeEntities, result.get(), "The returned income list should match the expected");
        verify(incomeRepository).findIncomeEntitiesByIncomeDateBetweenAndUserUserId(startDate, endDate, userId);
    }
}
