package pl.sonmiike.financewebapi.user;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(UserEntity user) {
        return UserDTO.builder()
                .id(user.getUserId())
                .username(user.getEmail())
                .email(user.getUsername())
                .roles(user.getAuthorities())
                .build();
    }

    public PagedUsersDTO toPagedDTO(Page<UserEntity> user) {
        return PagedUsersDTO.builder()
                .currentPage(user.getNumber())
                .totalPages(user.getTotalPages())
                .users(user.map(this::toDTO).getContent())
                .build();
    }
}
