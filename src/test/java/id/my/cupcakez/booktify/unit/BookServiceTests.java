package id.my.cupcakez.booktify.unit;

import com.querydsl.core.types.Predicate;
import id.my.cupcakez.booktify.domain.book.repository.IBookRepository;
import id.my.cupcakez.booktify.domain.book.service.BookService;
import id.my.cupcakez.booktify.dto.request.CreateBookRequest;
import id.my.cupcakez.booktify.dto.request.UpdateBookRequest;
import id.my.cupcakez.booktify.dto.response.BookResponse;
import id.my.cupcakez.booktify.entity.BookEntity;
import id.my.cupcakez.booktify.util.mapper.IBookMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookServiceTests {
    @Mock
    private IBookRepository bookRepository;

    @Spy // Wraps a real instance, allowing real method calls unless overridden with doReturn(), doAnswer(), doThrow() or doNothing()
    private IBookMapper bookMapper = Mappers.getMapper(IBookMapper.class); // // Real MapStruct mapper

    @InjectMocks
    private BookService bookService;

    private CreateBookRequest dataCreateRequest;
    private UpdateBookRequest dataUpdateRequest;
    private BookResponse dataCreateResponse;
    private BookResponse dataUpdateResponse;
    private BookEntity dataEntity;

    @BeforeEach
    public void setup(){
        dataCreateRequest = CreateBookRequest.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .description("The Great Gatsby is a 1925 novel by American writer F. Scott Fitzgerald.")
                .stock(10)
                .image("the-great-gatsby.jpg")
                .build();

        dataCreateResponse = BookResponse.builder()
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

        dataEntity = BookEntity.builder()
                .id(1L)
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
    @DisplayName("Test 1:Create Book Test")
    @Order(1)
    public void testCreateBook(){
        // given
        given(bookRepository.save(any(BookEntity.class))).willReturn(dataEntity);

        // when
        BookResponse result = bookService.createBook(dataCreateRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(dataCreateResponse);

        verify(bookRepository, times(1)).save(any(BookEntity.class));
        verify(bookMapper, times(1)).toBookResponse(any(BookEntity.class));
    }

    @Test
    @DisplayName("Test 2:Get Book By Id Test")
    @Order(2)
    public void testGetBookById(){
        // given
        given(bookRepository.findById(1L)).willReturn(java.util.Optional.of(dataEntity));

        // when
        BookResponse result = bookService.getBookById(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(dataCreateResponse);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).toBookResponse(any(BookEntity.class));
    }

    @Test
    @DisplayName("Test 3:Get Books Test")
    @Order(3)
    public void testGetBooks(){
        // given
        Page<BookEntity> bookEntities = new PageImpl<>(List.of(dataEntity));
        Pageable pageable = Pageable.ofSize(20).withPage(0);
        given(bookRepository.findAllByKeywords(eq(""), eq(pageable))).willReturn(bookEntities);

        // when
        Page<BookResponse> bookResponses = bookService.getBooks("", pageable);

        // then
        assertThat(bookResponses).isNotNull();
        assertThat(bookResponses.getContent()).hasSize(1);
        assertThat(bookResponses.getContent().get(0)).usingRecursiveComparison().isEqualTo(dataCreateResponse);

        verify(bookRepository, times(1)).findAllByKeywords(eq(""), eq(pageable));
        verify(bookMapper, times(1)).toBookResponse(any(BookEntity.class));
    }

    @Test
    @DisplayName("Test 4:Update Book Test")
    @Order(4)
    public void testUpdateBook(){
        // given
        given(bookRepository.findById(1L)).willReturn(Optional.of(dataEntity));
        given(bookRepository.save(any(BookEntity.class))).willReturn(dataEntity);

        // when
        BookResponse result = bookService.updateBook(1L, dataUpdateRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(dataUpdateResponse);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(BookEntity.class));
        verify(bookMapper, times(1)).toBookResponse(any(BookEntity.class));
    }

    @Test
    @DisplayName("Test 5:Delete Book Test")
    @Order(5)
    public void testDeleteBook(){
        // given
        doNothing().when(bookRepository).deleteById(1L);

        // when
        bookService.deleteBook(1L);

        // then
        verify(bookRepository, times(1)).deleteById(1L);
    }

}
