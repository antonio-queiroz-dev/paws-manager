package com.example.gestaoPetApi.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @LocalServerPort
    protected int port;

    static final MySQLContainer<?> mySQLContainer;
    static final GenericContainer<?> redisContainer;

    static {
        mySQLContainer = new MySQLContainer<>("mysql:8.0")
                .withDatabaseName("testedb")
                .withUsername("test")
                .withPassword("test");

        redisContainer = new  GenericContainer<>(DockerImageName.parse("redis:latest"))
                        .withExposedPorts(6379);


        mySQLContainer.start();
        redisContainer.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // MySQL
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.sql.init.mode", () -> "never");

        // Redis
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379).toString());
        registry.add("spring.cache.type", () -> "redis"); // Garante que o cache redis esteja ativo nos testes
    }

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
