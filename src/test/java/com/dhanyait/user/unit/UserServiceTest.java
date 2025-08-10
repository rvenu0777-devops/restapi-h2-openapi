package com.dhanyait.user.unit;

import com.dhanyait.user.dto.UserDto;
import com.dhanyait.user.model.User;
import com.dhanyait.user.repository.UserRepository;
import com.dhanyait.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    private final UserRepository repo = Mockito.mock(UserRepository.class);
    private final UserService svc = new UserService(repo);

    @Test
    void getAll_returnsMappedUsers() {
        Mockito.when(repo.findAll()).thenReturn(List.of(new User(1L, "Alice", "a@a.com")));
        var users = svc.getAll();
        assertThat(users).hasSize(1);
        assertThat(users.get(0).name()).isEqualTo("Alice");
    }

    @Test
    void create_duplicateEmail_throws() {
        Mockito.when(repo.findByEmail("a@a.com")).thenReturn(Optional.of(new User(1L,"Alice","a@a.com")));
        var dto = new UserDto(null, "Bob", "a@a.com");
        assertThatThrownBy(() -> svc.create(dto)).isInstanceOf(IllegalArgumentException.class);
    }
}
