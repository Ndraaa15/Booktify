package id.my.cupcakez.booktify.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.my.cupcakez.booktify.domain.book.controller.BookController;
import id.my.cupcakez.booktify.domain.book.repository.IBookRepository;
import id.my.cupcakez.booktify.domain.book.service.BookService;
import id.my.cupcakez.booktify.domain.book.service.IBookService;
import id.my.cupcakez.booktify.dto.request.CreateBookRequest;
import id.my.cupcakez.booktify.dto.request.UpdateBookRequest;
import id.my.cupcakez.booktify.dto.response.BookResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(BookController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@ContextConfiguration(classes = {BookController.class, IBookRepository.class, IBookService.class})
public class BookControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

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
                .image("the-great-gatsby.jpg")
                .build();

        dataUpdateRequest = UpdateBookRequest.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .stock(20)
                .image("the-great-gatsby.jpg")
                .build();


        dataResponse = BookResponse.builder()
                .id(1L)
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .stock(10)
                .image("the-great-gatsby.jpg")
                .build();

        dataUpdateResponse = BookResponse.builder()
                .id(1L)
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .stock(20)
                .image("the-great-gatsby.jpg")
                .build();
    }

    @Test
    @DisplayName("Test 1:Create Book Test")
    @Order(1)
    public void testCreateBook() throws Exception {
        // given
        given(bookService.createBook(any(CreateBookRequest.class))).willReturn(dataResponse);

        // then
        String bookJson = objectMapper.writeValueAsString(dataCreateRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/books")
                        .contentType("application/json")
                        .content(bookJson)
        ).andDo(
                print()
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.title").value(dataResponse.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.author").value(dataResponse.getAuthor())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.description").value(dataResponse.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.image").value(dataResponse.getImage())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.stock").value(dataResponse.getStock())
        );

        verify(bookService, times(1)).createBook(any(CreateBookRequest.class));
    }

    @Test
    @DisplayName("Test 2:Find All Books Test")
    @Order(2)
    public void testGetBooks() throws Exception {
        // given
        Page<BookResponse> bookResponses = new PageImpl<>(List.of(dataResponse));
        given(bookService.getBooks(any(), any())).willReturn(bookResponses);

        // then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/books")
        ).andDo(
                print()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.books.content[0].title").value(dataResponse.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.books.content[0].author").value(dataResponse.getAuthor())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.books.content[0].description").value(dataResponse.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.books.content[0].image").value(dataResponse.getImage())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.books.content[0].stock").value(dataResponse.getStock())
        );

        verify(bookService, times(1)).getBooks(any(), any());
    }

    @Test
    @DisplayName("Test 3:Find Book By Id Test")
    @Order(3)
    public void testGetBookById() throws Exception {
        // given
        given(bookService.getBookById(1L)).willReturn(dataResponse);

        // then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/books/1")
        ).andDo(
                print()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.title").value(dataResponse.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.author").value(dataResponse.getAuthor())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.description").value(dataResponse.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.image").value(dataResponse.getImage())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.stock").value(dataResponse.getStock())
        );

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    @DisplayName("Test 4:Update Book Test")
    @Order(4)
    public void testUpdateBook() throws Exception {
        // given
        given(bookService.updateBook(1L, dataUpdateRequest)).willReturn(dataUpdateResponse);

        // then
        String bookJson = objectMapper.writeValueAsString(dataUpdateRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/books/1")
                        .contentType("application/json")
                        .content(bookJson)
        ).andDo(
                print()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.title").value(dataUpdateResponse.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.author").value(dataUpdateResponse.getAuthor())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.description").value(dataUpdateResponse.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.image").value(dataUpdateResponse.getImage())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.book.stock").value(dataUpdateResponse.getStock())
        );

        verify(bookService, times(1)).updateBook(1L, dataUpdateRequest);
    }

    @Test
    @DisplayName("Test 5:Delete Book Test")
    @Order(5)
    public void testDeleteBook() throws Exception {
        // then
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/books/1")
        ).andDo(
                print()
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

        verify(bookService, times(1)).deleteBook(1L);
    }
}
