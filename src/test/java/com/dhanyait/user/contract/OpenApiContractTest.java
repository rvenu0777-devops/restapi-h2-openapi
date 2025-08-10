package com.dhanyait.user.contract;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OpenApiContractTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void openApi_containsUsersPaths() throws Exception {
        String url = "http://localhost:" + port + "/v3/api-docs";
        String json = rest.getForObject(new URI(url), String.class);
        ObjectMapper m = new ObjectMapper();
        JsonNode root = m.readTree(json);
        JsonNode paths = root.get("paths");
        assertThat(paths.has("/api/users")).isTrue();
        assertThat(paths.has("/api/users/{id}")).isTrue();
    }

    @Test
    @DisplayName("OpenAPI spec contains required endpoints and User schema")
    void openApiContainsContract() throws Exception {
        String url = "http://localhost:" + port + "/v3/api-docs";
        String json = rest.getForObject(new URI(url), String.class);
        JsonNode root = mapper.readTree(json);

        // Check that essential endpoints exist
        JsonNode paths = root.path("paths");
        assertThat(paths.has("/api/users")).as("GET /api/users should exist").isTrue();
        assertThat(paths.has("/api/users/{id}")).as("GET /api/users/{id} should exist").isTrue();

        // Check that UserDto schema is present in components
        JsonNode schemas = root.path("components").path("schemas");
        assertThat(schemas.has("UserDto")).as("UserDto schema should be defined").isTrue();

        // Optional: verify 'email' field exists in UserDto schema
        JsonNode emailProp = schemas.path("UserDto").path("properties").path("email");
        assertThat(emailProp.isMissingNode()).as("'email' property should exist").isFalse();
    }
}
