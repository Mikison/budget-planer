package pl.sonmiike.reportsservice.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class UserReportServiceTest {


    @Mock
    private UserReportRepository userReportRepository;

    @InjectMocks
    private UserReportService userReportService;


    @BeforeEach
    void setUp() {
        openMocks(this);
    }


    @Test
    void testFetchAllUsers_ReturnsAllUsers() {
        // Arrange
        Set<UserReport> mockUserReports = Set.of(new UserReport(1L), new UserReport(2L));

        when(userReportRepository.findAll()).thenReturn(new ArrayList<>(mockUserReports));

        // Act
        Set<UserReport> result = userReportService.fetchAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(mockUserReports));
        verify(userReportRepository).findAll();
    }

    @Test
    void testFetchUserById_WithValidId_ReturnsUserReport() {
        Long userId = 1L;
        UserReport expectedUserReport = new UserReport(userId);
        when(userReportRepository.findById(userId)).thenReturn(Optional.of(expectedUserReport));

        Optional<UserReport> result = userReportService.fetchUserById(userId);

        assertTrue(result.isPresent(), "Expected result to be present");
        assertEquals(expectedUserReport, result.get(), "Expected result to match the expected user report");
        verify(userReportRepository).findById(userId);
    }

    @Test
    void testFetchUserById_WithInvalidId_ReturnsEmptyOptional() {
        Long userId = 99L;
        when(userReportRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<UserReport> result = userReportService.fetchUserById(userId);

        assertFalse(result.isPresent(), "Expected result to be empty");
        verify(userReportRepository).findById(userId);
    }
}
