package id.my.cupcakez.booktify.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.my.cupcakez.booktify.constant.UserRole;
import id.my.cupcakez.booktify.domain.auth.service.IAuthService;
import id.my.cupcakez.booktify.domain.user.repository.IUserRepository;
import id.my.cupcakez.booktify.dto.request.CreateBookRequest;
import id.my.cupcakez.booktify.dto.request.UpdateBookRequest;
import id.my.cupcakez.booktify.dto.request.UserLoginRequest;
import id.my.cupcakez.booktify.dto.request.UserRegisterRequest;
import id.my.cupcakez.booktify.dto.response.BookResponse;
import id.my.cupcakez.booktify.entity.UserEntity;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String token;

    private CreateBookRequest dataCreateRequest;
    private UpdateBookRequest dataUpdateRequest;
    private RestTemplate restTemplate;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }


    @BeforeAll
    static void beforeAll(@Autowired IUserRepository userRepository, @Autowired IAuthService authService, @Autowired BCryptPasswordEncoder bCryptPasswordEncoder) {
        UserEntity user = UserEntity.builder()
                .name("John Doe")
                .email("testuser@example.com")
                .password(bCryptPasswordEncoder.encode("TestPassword123!"))
                .role(UserRole.STAFF)
                .phone("+6281234567890")
                .address("Jl. Raya Kuta No. 1")
                .build();

        userRepository.save(user);

        UserLoginRequest login = UserLoginRequest.builder()
                .email("testuser@example.com")
                .password("TestPassword123!")
                .build();

        token = authService.login(login).getToken();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.flyway.enabled", () -> "false");
    }

    @BeforeEach
    public void setup(){
        MockMvcClientHttpRequestFactory requestFactory = new MockMvcClientHttpRequestFactory(mockMvc);
        restTemplate = new RestTemplate(requestFactory);

        dataCreateRequest = CreateBookRequest.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .stock(10)
                .image("https://www.example.com/the-great-gatsby.jpg")
                .build();

        dataUpdateRequest = UpdateBookRequest.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .stock(20)
                .image("https://www.example.com/the-great-gatsby.jpg")
                .build();
    }

    @Test
    @DisplayName("Test 1: Create Book Test")
    @Order(1)
    public void testCreateBook() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<CreateBookRequest> httpRequestBody = new HttpEntity<>(dataCreateRequest, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate
                .exchange(
                        "/api/v1/books",
                        HttpMethod.POST,
                        httpRequestBody, new ParameterizedTypeReference<Map<String, Object>>() {});

        JsonNode jsonBookResponse = objectMapper.readTree(objectMapper.writeValueAsString(response.getBody()));
        BookResponse bookResponse = objectMapper.convertValue(jsonBookResponse.path("data"), BookResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertThat(bookResponse).extracting(
                BookResponse::getTitle,
                BookResponse::getAuthor,
                BookResponse::getDescription,
                BookResponse::getStock,
                BookResponse::getImage
        ).containsExactly(dataCreateRequest.getTitle(), dataCreateRequest.getAuthor(), dataCreateRequest.getDescription(), dataCreateRequest.getStock(), dataCreateRequest.getImage());
    }

    @Test
    @DisplayName("Test 2: Get Book By Id Test")
    @Order(2)
    public void testGetBookById() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<CreateBookRequest> httpRequestBody = new HttpEntity<>(null, headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/api/v1/books/1", HttpMethod.GET, httpRequestBody, new ParameterizedTypeReference<Map<String, Object>>() {});

        JsonNode jsonBookResponse = objectMapper.readTree(objectMapper.writeValueAsString(response.getBody()));
        BookResponse bookResponse = objectMapper.convertValue(jsonBookResponse.path("data"), BookResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(bookResponse).extracting(
                BookResponse::getTitle,
                BookResponse::getAuthor,
                BookResponse::getDescription,
                BookResponse::getStock,
                BookResponse::getImage
        ).containsExactly(dataCreateRequest.getTitle(), dataCreateRequest.getAuthor(), dataCreateRequest.getDescription(), dataCreateRequest.getStock(), dataCreateRequest.getImage());
    }

    @Test
    @DisplayName("Test 3: Get All Books Test")
    @Order(3)
    public void testGetBooks() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<CreateBookRequest> httpRequestBody = new HttpEntity<>(null, headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/api/v1/books", HttpMethod.GET, httpRequestBody, new ParameterizedTypeReference<Map<String, Object>>() {});


        JsonNode jsonBookResponse = objectMapper.readTree(objectMapper.writeValueAsString(response.getBody()));
        BookResponse[] bookResponses = objectMapper.convertValue(jsonBookResponse.path("data").path("content"), BookResponse[].class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(bookResponses).isNotEmpty();
        assertThat(bookResponses).hasSize(1);
        assertThat(bookResponses[0]).extracting(
                BookResponse::getTitle,
                BookResponse::getAuthor,
                BookResponse::getDescription,
                BookResponse::getStock,
                BookResponse::getImage
        ).containsExactly(dataCreateRequest.getTitle(), dataCreateRequest.getAuthor(), dataCreateRequest.getDescription(), dataCreateRequest.getStock(), dataCreateRequest.getImage());
    }

    @Test
    @DisplayName("Test 4: Update Book Test")
    @Order(4)
    public void testUpdateBook() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<UpdateBookRequest> httpRequestBody = new HttpEntity<>(dataUpdateRequest, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/api/v1/books/1", HttpMethod.PATCH, httpRequestBody, new ParameterizedTypeReference<Map<String, Object>>() {});

        JsonNode jsonBookResponse = objectMapper.readTree(objectMapper.writeValueAsString(response.getBody()));
        BookResponse bookResponse = objectMapper.convertValue(jsonBookResponse.path("data"), BookResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(bookResponse).extracting(
                BookResponse::getTitle,
                BookResponse::getAuthor,
                BookResponse::getDescription,
                BookResponse::getStock,
                BookResponse::getImage
        ).containsExactly(dataUpdateRequest.getTitle(), dataUpdateRequest.getAuthor(), dataUpdateRequest.getDescription(), dataUpdateRequest.getStock(), dataUpdateRequest.getImage());
    }

    @Test
    @DisplayName("Test 5: Delete Book Test")
    @Order(5)
    public void testDeleteBook() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<CreateBookRequest> httpRequestBody = new HttpEntity<>(null, headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/api/v1/books/1", HttpMethod.DELETE, httpRequestBody, new ParameterizedTypeReference<Map<String, Object>>() {});

        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
    }



    @Test
    @DisplayName("Test 6: Get Book By Id Not Found Test")
    @Order(6)
    public void testGetBookByIdNotFound() throws JsonProcessingException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<CreateBookRequest> httpRequestBody = new HttpEntity<>(null, headers);
            restTemplate.exchange("/api/v1/books/1", HttpMethod.GET, httpRequestBody, new ParameterizedTypeReference<Map<String, Object>>() {});
        } catch (HttpClientErrorException e) {
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }

}
