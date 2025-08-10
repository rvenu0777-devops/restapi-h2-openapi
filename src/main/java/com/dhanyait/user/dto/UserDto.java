package com.dhanyait.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
        Long id,
        @NotBlank String name,
        @NotBlank @Email String email
) {}
