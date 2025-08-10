package com.dhanyait.user.integration;

import com.dhanyait.user.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private String base() { return "http://localhost:" + port + "/api/users"; }

    @Test
    public void createAndGetUser() {
        UserDto create = new UserDto(null, "Integration", "int@ex.com");
        ResponseEntity<UserDto> res = rest.postForEntity(base(), create, UserDto.class);
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UserDto created = res.getBody();
        assertThat(created).isNotNull();
        Long id = created.id();

        ResponseEntity<UserDto> get = rest.getForEntity(base() + "/" + id, UserDto.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody().name()).isEqualTo("Integration");
    }
}
