package pl.sonmiike.financewebapi.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sonmiike.financewebapi.category.UserCategoryRepository;
import pl.sonmiike.financewebapi.exceptions.custom.ResourceNotFoundException;
import pl.sonmiike.financewebapi.expenses.ExpenseRepository;
import pl.sonmiike.financewebapi.income.IncomeRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final UserCategoryRepository userCategoryRepository;

    private final UserMapper userMapper;


    public PagedUsersDTO fetchAllUsers(int page, int size) {
        Page<UserEntity> users = userRepository.findAll(PageRequest.of(page, size));
        return userMapper.toPagedDTO(users);
    }

    public UserEntity fetchUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found in the database"));
    }


    public UserDTO fetchUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found in the database"));
    }


    @Transactional
    public void deleteUserById(Long userid) {
        if (!userRepository.existsById(userid)) {
            throw new ResourceNotFoundException("User not found in the database");
        }
        incomeRepository.deleteAllByUserUserId(userid);
        expenseRepository.deleteAllByUserUserId(userid);
        userCategoryRepository.deleteAllByUserUserId(userid);
        userRepository.deleteById(userid);

    }

}
