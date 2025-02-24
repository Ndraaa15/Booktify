package id.my.cupcakez.booktify.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.my.cupcakez.booktify.domain.auth.service.IAuthService;
import id.my.cupcakez.booktify.domain.book.service.IBookService;
import id.my.cupcakez.booktify.dto.request.CreateBookRequest;
import id.my.cupcakez.booktify.dto.request.CreateRentRequest;
import id.my.cupcakez.booktify.dto.request.UserLoginRequest;
import id.my.cupcakez.booktify.dto.request.UserRegisterRequest;
import id.my.cupcakez.booktify.dto.response.BookResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RentIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IBookService bookService;

    @Autowired
    private IAuthService authService;

    private Long bookId;
    private String token;

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

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    public void setup() {
        UserRegisterRequest user = UserRegisterRequest.builder()
                .name("John Doe")
                .email("testuser@example.com")
                .password("TestPassword123!")
                .confirmPassword("TestPassword123!")
                .phone("+6281234567890")
                .address("Jl. Raya Kuta No. 1")
                .build();

        authService.register(user);

        UserLoginRequest login = UserLoginRequest.builder()
                .email("testuser@example.com")
                .password("TestPassword123!")
                .build();

        token = authService.login(login).getToken();

        CreateBookRequest book = CreateBookRequest.builder()
                .title("Spring Boot Concurrency")
                .author("John Doe")
                .description("Testing concurrency in Spring Boot.")
                .image("https://example.com/spring-boot-concurrency.jpg")
                .stock(1)
                .build();

        BookResponse bookSaved = bookService.createBook(book);
        bookId = bookSaved.getId();
    }

    @Test
    @Order(1)
    void testConcurrentBookRent() throws InterruptedException, JsonProcessingException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);
        List<Future<MvcResult>> results = new ArrayList<>();

        CreateRentRequest rent = CreateRentRequest.builder()
                .quantity(1)
                .rentedUntil("20-12-2025")
                .bookId(bookId)
                .build();

        String rentJson = objectMapper.writeValueAsString(rent);

        Callable<MvcResult> task = () -> {
            try {
                return mockMvc.perform(
                                MockMvcRequestBuilders.post("/api/v1/rents")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(rentJson)
                                        .header("Authorization", "Bearer " + token)
                        )
                        .andDo(print())
                        .andReturn();
            } finally {
                latch.countDown();
            }
        };

        results.add(executor.submit(task));
        results.add(executor.submit(task));

        latch.await();
        executor.shutdown();

        List<Integer> statusCodes = new ArrayList<>();
        for (Future<MvcResult> result : results) {
            int status =  result.get().getResponse().getStatus();
            statusCodes.add(status);
        }

        BookResponse updatedBook = bookService.getBookById(bookId);
        assertThat(updatedBook.getStock()).isEqualTo(0);
        assertThat(statusCodes).contains(201, 400);
    }
}
