package id.my.cupcakez.booktify.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.my.cupcakez.booktify.domain.book.controller.BookController;
import id.my.cupcakez.booktify.domain.book.repository.BookQueryFilter;
import id.my.cupcakez.booktify.domain.book.service.BookService;
import id.my.cupcakez.booktify.dto.request.CreateBookRequest;
import id.my.cupcakez.booktify.dto.request.UpdateBookRequest;
import id.my.cupcakez.booktify.dto.response.BookResponse;
import id.my.cupcakez.booktify.exception.CustomException;
import id.my.cupcakez.booktify.response.ResponseWrapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateBookRequest dataCreateRequest;
    private UpdateBookRequest dataUpdateRequest;
    private BookResponse dataResponse;
    private BookResponse dataUpdateResponse;

    @BeforeEach
    public void setup(){
        dataCreateRequest = CreateBookRequest.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .stock(10)
                .image("http://example.com/the-great-gatsby.jpg")
                .build();

        dataUpdateRequest = UpdateBookRequest.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .stock(20)
                .image("http://example.com/the-great-gatsby.jpg")
                .build();


        dataResponse = BookResponse.builder()
                .id(1L)
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .stock(10)
                .image("http://example.com/the-great-gatsby.jpg")
                .build();

        dataUpdateResponse = BookResponse.builder()
                .id(1L)
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .stock(20)
                .image("http://example.com/the-great-gatsby.jpg")
                .build();
    }

    @Test
    @DisplayName("Test 1:Create Book Test")
    @Order(1)
    public void testCreateBook() throws Exception {
        // given
        given(bookService.createBook(any(CreateBookRequest.class))).willReturn(dataResponse);

        // when
        ResponseEntity<ResponseWrapper<BookResponse>> bookResponse =bookController.createBook(dataCreateRequest);

        // then
        assertThat(bookResponse.getBody().getData()).extracting(
                BookResponse::getTitle,
                BookResponse::getAuthor,
                BookResponse::getDescription,
                BookResponse::getImage,
                BookResponse::getStock
        ).contains(
                dataResponse.getTitle(),
                dataResponse.getAuthor(),
                dataResponse.getDescription(),
                dataResponse.getImage(),
                dataResponse.getStock()
        );

        verify(bookService, times(1)).createBook(any(CreateBookRequest.class));
    }

    @Test
    @DisplayName("Test 2:Find All Books Test")
    @Order(2)
    public void testGetBooks() {
        // given
        Page<BookResponse> bookMockResponses = new PageImpl<>(List.of(dataResponse));
        Pageable pageable = Pageable.ofSize(20).withPage(0);;
        BookQueryFilter bookQueryFilter = BookQueryFilter.builder().keyword("").pageable(pageable).build();
        given(bookService.findBooks(bookQueryFilter)).willReturn(bookMockResponses);

        // when
        ResponseEntity<ResponseWrapper<PagedModel<BookResponse>>> bookResponses = bookController.findBooks("", pageable);

        // then
        assertThat(bookResponses.getBody().getData().getContent()).hasSize(1);
        assertThat(bookResponses.getBody().getData().getContent().get(0)).extracting(
                BookResponse::getTitle,
                BookResponse::getAuthor,
                BookResponse::getDescription,
                BookResponse::getImage,
                BookResponse::getStock
        ).contains(
                dataResponse.getTitle(),
                dataResponse.getAuthor(),
                dataResponse.getDescription(),
                dataResponse.getImage(),
                dataResponse.getStock()
        );


        verify(bookService, times(1)).findBooks(bookQueryFilter);
    }

    @Test
    @DisplayName("Test 3:Find Book By Id Test")
    @Order(3)
    public void testGetBookById() {
        // given
        given(bookService.findBookById(1L)).willReturn(dataResponse);

        // when
        ResponseEntity<ResponseWrapper<BookResponse>> bookResponse = bookController.findBookById(1L);

        // then
        assertThat(bookResponse.getBody().getData()).extracting(
                BookResponse::getTitle,
                BookResponse::getAuthor,
                BookResponse::getDescription,
                BookResponse::getImage,
                BookResponse::getStock
        ).contains(
                dataResponse.getTitle(),
                dataResponse.getAuthor(),
                dataResponse.getDescription(),
                dataResponse.getImage(),
                dataResponse.getStock()
        );

        verify(bookService, times(1)).findBookById(1L);
    }

    @Test
    @DisplayName("Test 4:Update Book Test")
    @Order(4)
    public void testUpdateBook() {
        // given
        given(bookService.updateBook(1L, dataUpdateRequest)).willReturn(dataUpdateResponse);

        // when
        ResponseEntity<ResponseWrapper<BookResponse>> bookResponse = bookController.updateBook(1L, dataUpdateRequest);

        // then
        assertThat(bookResponse.getBody().getData()).extracting(
                BookResponse::getTitle,
                BookResponse::getAuthor,
                BookResponse::getDescription,
                BookResponse::getImage,
                BookResponse::getStock
        ).contains(
                dataUpdateResponse.getTitle(),
                dataUpdateResponse.getAuthor(),
                dataUpdateResponse.getDescription(),
                dataUpdateResponse.getImage(),
                dataUpdateResponse.getStock()
        );

        verify(bookService, times(1)).updateBook(1L, dataUpdateRequest);
    }

    @Test
    @DisplayName("Test 5:Delete Book Test")
    @Order(5)
    public void testDeleteBook() {
        // given
        doNothing().when(bookService).deleteBook(1L);

        // when
        ResponseEntity<?> bookResponse = bookController.deleteBook(1L);

        // then
        assertThat(bookResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    @DisplayName("Test 6:Find Book By Id Not Found Test")
    @Order(6)
    public void testGetBookNotFound(){
        // given
        given(bookService.findBookById(2L)).willThrow(new CustomException("Book not found", HttpStatus.NOT_FOUND));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            bookController.findBookById(2L);
        });

        // then
        assertEquals("Book not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(bookService, times(1)).findBookById(2L);
    }
}
