package com.dhanyait.user.service;

import com.dhanyait.user.dto.UserDto;
import com.dhanyait.user.model.User;
import com.dhanyait.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<UserDto> getAll() {
        return repo.findAll().stream()
                .map(u -> new UserDto(u.getId(), u.getName(), u.getEmail()))
                .collect(Collectors.toList());
    }

    public UserDto getById(Long id) {
        return repo.findById(id)
                .map(u -> new UserDto(u.getId(), u.getName(), u.getEmail()))
                .orElse(null);
    }

    public UserDto create(UserDto dto) {
        // Basic example: check unique email
        repo.findByEmail(dto.email()).ifPresent(u -> {
            throw new IllegalArgumentException("email already exists");
        });

        User saved = repo.save(new User(dto.name(), dto.email()));
        return new UserDto(saved.getId(), saved.getName(), saved.getEmail());
    }
}
