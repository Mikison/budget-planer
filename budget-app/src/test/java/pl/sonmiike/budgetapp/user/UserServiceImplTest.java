package pl.sonmiike.budgetapp.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pl.sonmiike.budgetapp.category.UserCategoryRepository;
import pl.sonmiike.budgetapp.exceptions.custom.ResourceNotFoundException;
import pl.sonmiike.budgetapp.expenses.ExpenseRepository;
import pl.sonmiike.budgetapp.income.IncomeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private UserCategoryRepository userCategoryRepository;


    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testFetchAllUsers() {
        // Prepare your test data
        List<UserEntity> userList = List.of(
                UserEntity.builder().userId(1L).build(),
                UserEntity.builder().userId(2L).build(),
                UserEntity.builder().userId(3L).build()
        );
        int page = 0;
        int size = 5;

        Page<UserEntity> userPage = new PageImpl<>(userList, PageRequest.of(page, size), userList.size());
        when(userRepository.findAll(PageRequest.of(page, size))).thenReturn(userPage);

        when(userMapper.toDTO(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity userEntity = invocation.getArgument(0);
            return UserDTO.builder()
                    .id(userEntity.getUserId())
                    .build();
        });

        PagedUsersDTO expectedDTO = PagedUsersDTO.builder()
                .currentPage(0)
                .totalPages(1)
                .users(userList.stream().map(userMapper::toDTO).collect(Collectors.toList()))
                .build();

        when(userMapper.toPagedDTO(userPage)).thenReturn(expectedDTO);

        PagedUsersDTO actualDTO = userService.fetchAllUsers(page, size);

        assertEquals(expectedDTO.getCurrentPage(), actualDTO.getCurrentPage());
        assertEquals(expectedDTO.getTotalPages(), actualDTO.getTotalPages());
        assertNotNull(actualDTO.getUsers());
    }

    @Test
    void testFetchUserById_Success() {
        Long userId = 1L;
        UserEntity userEntity = UserEntity.builder().userId(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));


        UserEntity userById = userService.fetchUserById(userId);
        assertNotNull(userById);
        assertEquals(userById, userEntity);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFetchUserById_Failure() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.fetchUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testgetUserByEmail_Success() {
        String email = "test@test.com";
        UserEntity userEntity = UserEntity.builder().email(email).build();
        UserDTO dto = UserDTO.builder().email(email).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDTO(userEntity)).thenReturn(dto);

        UserDTO userByEmail = userService.fetchUserByEmail(email);

        assertEquals(userByEmail, dto);
        verify(userRepository, times(1)).findByEmail(email);

    }

    @Test
    void testgetUserByEmail_Failure() {
        String email = "test@test.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.fetchUserByEmail(email));
        verify(userRepository, times(1)).findByEmail(email);

    }

    @Test
    void shouldDeleteUserAndRelatedDataWhenUserExists() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUserById(userId);

        verify(userRepository).deleteById(userId);
        verify(incomeRepository).deleteAllByUserUserId(userId);
        verify(expenseRepository).deleteAllByUserUserId(userId);
        verify(userCategoryRepository).deleteAllByUserUserId(userId);

    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        Long userId = 2L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserById(userId));

        verify(userRepository, never()).deleteById(anyLong());
        verify(incomeRepository, never()).deleteAllByUserUserId(anyLong());
        verify(expenseRepository, never()).deleteAllByUserUserId(anyLong());
        verify(userCategoryRepository, never()).deleteAllByUserUserId(anyLong());
    }
}
