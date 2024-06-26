package pl.sonmiike.budgetapp.user;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    @GetMapping
    public ResponseEntity<PagedUsersDTO> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.fetchAllUsers(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserEntity userById = userService.fetchUserById(id);
        return ResponseEntity.ok(userMapper.toDTO(userById));
    }

    @GetMapping("/email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        UserDTO userByEmail = userService.fetchUserByEmail(email);
        return ResponseEntity.ok(userByEmail);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }


}
