package com.pawsmanager.api.integration;

import com.pawsmanager.api.user.User;
import com.pawsmanager.api.user.UserRole;
import com.pawsmanager.api.user.UserRepository;
import com.pawsmanager.api.auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.springframework.http.HttpHeaders;

// classe base para todos os testes de integração
// sobe containers reais de MySQL e Redis via Testcontainers
// porta aleatória para evitar conflito com a aplicação rodando localmente
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    // porta gerada automaticamente pelo Spring para cada execução de teste
    @LocalServerPort
    protected int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    private String token;

    @BeforeEach
    void criarUsuarioDeTest() {
        // remove o usuario do teste anterior se ele existir
        userRepository.findByEmail("test@test.com").ifPresent(userRepository::delete);

        // cria um usuario ADMIN diretamente no banco
        User admin = new User("test@test.com", passwordEncoder.encode("senha1234"), UserRole.ADMIN);

        // salva no banco
        userRepository.saveAndFlush(admin);
        token = jwtService.generateToken(admin);
    }


    // containers estáticos inicializados uma vez e reutilizados em todos os testes
    static final MySQLContainer<?> mySQLContainer;
    static final GenericContainer<?> redisContainer;

    static {
        mySQLContainer = new MySQLContainer<>(
                "mysql:8.0")
                .withDatabaseName("testedb")
                .withUsername("test")
                .withPassword("test");

        redisContainer = new GenericContainer<>(DockerImageName.parse("redis:latest"))
                .withExposedPorts(6379);

        mySQLContainer.start();
        redisContainer.start();
    }

    // sobrescreve as propriedades do application-test.properties com os valores dos containers
    // necessário porque as portas são dinâmicas — o Testcontainers escolhe uma porta livre
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        // create-drop: cria as tabelas antes dos testes e apaga ao final
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.sql.init.mode", () -> "never");
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379).toString());
        registry.add("spring.cache.type", () -> "redis");
    }

    // monta o header Authorization com o token gerado no setUp
    protected HttpHeaders headersComToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
}