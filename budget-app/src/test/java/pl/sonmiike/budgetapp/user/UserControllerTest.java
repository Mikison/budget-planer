package pl.sonmiike.budgetapp.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.sonmiike.budgetapp.security.auth.JwtService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private JwtService jwtService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {

    }

    @Test
    void getAllUsersTest() throws Exception {
        
        int page = 0;
        int size = 10;
        PagedUsersDTO pagedUsersDTO = mock(PagedUsersDTO.class); // Mock the DTO
        when(userService.fetchAllUsers(page, size)).thenReturn(pagedUsersDTO);

        
        mockMvc.perform(get("/api/v1/admin/users?page=" + page + "&size=" + size))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pagedUsersDTO)));

        verify(userService).fetchAllUsers(page, size);
    }

    @Test
    void getUserByIdTest() throws Exception {
        
        Long id = 1L;
        UserEntity userEntity = mock(UserEntity.class);
        UserDTO userDTO = mock(UserDTO.class);
        when(userService.fetchUserById(id)).thenReturn(userEntity);
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);

        
        mockMvc.perform(get("/api/v1/admin/users/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));

        verify(userService).fetchUserById(id);
        verify(userMapper).toDTO(userEntity);
    }

    @Test
    void getUserByEmailTest() throws Exception {
        
        String email = "test@example.com";
        UserDTO userDTO = mock(UserDTO.class);
        when(userService.fetchUserByEmail(email)).thenReturn(userDTO);

        
        mockMvc.perform(get("/api/v1/admin/users/email").param("email", email))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));

        verify(userService).fetchUserByEmail(email);
    }

    @Test
    void deleteUserByIdTest() throws Exception {
        
        Long id = 1L;

        
        mockMvc.perform(delete("/api/v1/admin/users/" + id))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(id);
    }
}
