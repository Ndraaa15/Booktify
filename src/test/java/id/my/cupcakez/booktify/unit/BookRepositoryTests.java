package id.my.cupcakez.booktify.unit;


import id.my.cupcakez.booktify.domain.book.repository.IBookRepository;
import id.my.cupcakez.booktify.entity.BookEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional // Ensures rollback after each test
public class BookRepositoryTests {

    @Autowired
    private IBookRepository bookRepository;

    private BookEntity data;

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
        assertThat(savedBook).usingRecursiveComparison().ignoringFields("id").isEqualTo(data);
    }

    @Test
    @DisplayName("Test 2: Find All Books Test")
    @Order(2)
    public void testGetBooks() {
        // Given
        bookRepository.save(data);

        // When
        List<BookEntity> books = bookRepository.findAll();

        // Then
        assertThat(books).isNotEmpty();
        assertThat(books).hasSize(1);
        assertThat(books.get(0)).usingRecursiveComparison().ignoringFields("id").isEqualTo(data);
    }

    @Test
    @DisplayName("Test 3: Find Book By Id Test")
    @Order(3)
    public void testGetBookById() {
        // Given
        BookEntity savedBook = bookRepository.save(data);

        // When
        Optional<BookEntity> foundBook = bookRepository.findById(savedBook.getId());

        // Then
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get()).usingRecursiveComparison().isEqualTo(savedBook);
    }

    @Test
    @DisplayName("Test 4: Update Book Test")
    @Order(4)
    public void testUpdateBook() {
        // Given
        BookEntity savedBook = bookRepository.save(data);
        savedBook.setStock(20);

        // When
        BookEntity updatedBook = bookRepository.save(savedBook);

        // Then
        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getStock()).isEqualTo(20);
    }

    @Test
    @DisplayName("Test 5: Delete Book Test")
    @Order(5)
    public void testDeleteBook() {
        // Given
        BookEntity savedBook = bookRepository.save(data);
        Long bookId = savedBook.getId();

        // When
        bookRepository.deleteById(bookId);
        Optional<BookEntity> deletedBook = bookRepository.findById(bookId);

        // Then
        assertThat(deletedBook).isEmpty();
    }
}
