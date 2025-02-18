package id.my.cupcakez.booktify.integration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.my.cupcakez.booktify.domain.book.repository.IBookRepository;
import id.my.cupcakez.booktify.domain.book.service.BookService;
import id.my.cupcakez.booktify.domain.book.service.IBookService;
import id.my.cupcakez.booktify.dto.request.CreateBookRequest;
import id.my.cupcakez.booktify.dto.request.UpdateBookRequest;
import id.my.cupcakez.booktify.dto.response.BookResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockMvcClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateBookRequest dataCreateRequest;
    private UpdateBookRequest dataUpdateRequest;

    private RestTemplate restTemplate;

    @BeforeEach
    public void setup(){
        MockMvcClientHttpRequestFactory requestFactory = new MockMvcClientHttpRequestFactory(mockMvc);
        restTemplate = new RestTemplate(requestFactory);

        dataCreateRequest = CreateBookRequest.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .stock(10)
                .image("the-great-gatsby.jpg")
                .build();

        dataUpdateRequest = UpdateBookRequest.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .stock(20)
                .image("the-great-gatsby.jpg")
                .build();
    }

    @Test
    @DisplayName("Test 1: Create Book Test")
    @Order(1)
    public void testCreateBook() throws Exception {
        HttpEntity<CreateBookRequest> httpRequestBody = new HttpEntity<>(dataCreateRequest);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/api/v1/books", HttpMethod.POST, httpRequestBody, new ParameterizedTypeReference<Map<String, Object>>() {});

        JsonNode jsonBookResponse = objectMapper.readTree(objectMapper.writeValueAsString(response.getBody()));
        BookResponse bookResponse = objectMapper.convertValue(jsonBookResponse.path("book"), BookResponse.class);

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
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/api/v1/books/1", HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});

        JsonNode jsonBookResponse = objectMapper.readTree(objectMapper.writeValueAsString(response.getBody()));
        BookResponse bookResponse = objectMapper.convertValue(jsonBookResponse.path("book"), BookResponse.class);

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
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/api/v1/books", HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {});

        JsonNode jsonBookResponse = objectMapper.readTree(objectMapper.writeValueAsString(response.getBody()));
        BookResponse[] bookResponses = objectMapper.convertValue(jsonBookResponse.path("books").path("content"), BookResponse[].class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(bookResponses).isNotEmpty();
    }

    @Test
    @DisplayName("Test 4: Update Book Test")
    @Order(4)
    public void testUpdateBook() throws Exception {
        HttpEntity<UpdateBookRequest> httpRequestBody = new HttpEntity<>(dataUpdateRequest);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/api/v1/books/1", HttpMethod.PATCH, httpRequestBody, new ParameterizedTypeReference<Map<String, Object>>() {});

        JsonNode jsonBookResponse = objectMapper.readTree(objectMapper.writeValueAsString(response.getBody()));
        BookResponse bookResponse = objectMapper.convertValue(jsonBookResponse.path("book"), BookResponse.class);

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
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/api/v1/books/1", HttpMethod.DELETE, null, new ParameterizedTypeReference<Map<String, Object>>() {});

        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
    }
}
