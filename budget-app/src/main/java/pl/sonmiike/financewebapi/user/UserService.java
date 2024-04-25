package pl.sonmiike.financewebapi.user;

public interface UserService {

    PagedUsersDTO fetchAllUsers(int page, int size);

    UserEntity fetchUserById(Long id);

    UserDTO fetchUserByEmail(String email);

    void deleteUserById(Long userid);
}
