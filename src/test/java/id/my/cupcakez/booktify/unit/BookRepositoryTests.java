package id.my.cupcakez.booktify.unit;


import id.my.cupcakez.booktify.domain.book.repository.IBookRepository;
import id.my.cupcakez.booktify.entity.BookEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class BookRepositoryTests {
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

    @Autowired
    private IBookRepository bookRepository;

    private BookEntity data;

    @BeforeAll
    static void setup(@Autowired IBookRepository bookRepository) {
        BookEntity data = BookEntity.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .image("the-great-gatsby.jpg")
                .stock(10)
                .build();
        bookRepository.save(data);
    }

    @BeforeEach
    public void setup(){
        data = BookEntity.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .image("the-great-gatsby.jpg")
                .stock(10)
                .build();
    }

    @Test
    @DisplayName("Test 1: Save Book Test")
    @Order(1)
    public void testCreateBook() {
        // When
        BookEntity savedBook = bookRepository.save(data);

        // Then
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(data);
    }

    @Test
    @DisplayName("Test 2: Find All Books Test")
    @Order(2)
    public void testGetBooks() {
        // When
        List<BookEntity> books = bookRepository.findAll();

        // Then
        assertThat(books).isNotEmpty();
        assertThat(books).hasSize(1);
        assertThat(books.get(0)).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(data);
    }

    @Test
    @DisplayName("Test 3: Find Book By Id Test")
    @Order(3)
    public void testGetBookById() {
        // When
        Optional<BookEntity> foundBook = bookRepository.findById(1L);

        // Then
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get()).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(data);
    }

    @Test
    @DisplayName("Test 4: Update Book Test")
    @Order(4)
    public void testUpdateBook() {
        // Given
        BookEntity book = bookRepository.getById(1L);
        book.setStock(20);

        // When
        BookEntity updatedBook = bookRepository.save(book);

        // Then
        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getStock()).isEqualTo(20);
    }

    @Test
    @DisplayName("Test 5: Delete Book Test")
    @Order(5)
    public void testDeleteBook() {
        // When
        bookRepository.deleteById(1L);
        Optional<BookEntity> deletedBook = bookRepository.findById(1L);

        // Then
        assertThat(deletedBook).isEmpty();
    }
}
