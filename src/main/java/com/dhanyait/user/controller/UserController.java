package  com.dhanyait.user.controller;

import com.dhanyait.user.dto.UserDto;
import com.dhanyait.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User operations")
public class UserController {

    private final UserService svc;

    public UserController(UserService svc) {
        this.svc = svc;
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(svc.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by id")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto dto = svc.getById(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(summary = "Create a user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto dto) {
        UserDto created = svc.create(dto);
        return ResponseEntity.created(URI.create("/api/users/" + created.id())).body(created);
    }
}
