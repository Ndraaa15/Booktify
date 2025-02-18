package id.my.cupcakez.booktify.domain.book.service;
import id.my.cupcakez.booktify.domain.book.repository.IBookRepository;
import id.my.cupcakez.booktify.dto.request.CreateBookRequest;
import id.my.cupcakez.booktify.dto.request.UpdateBookRequest;
import id.my.cupcakez.booktify.dto.response.BookResponse;
import id.my.cupcakez.booktify.entity.BookEntity;
import id.my.cupcakez.booktify.exception.CustomException;
import id.my.cupcakez.booktify.util.mapper.IBookMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService implements IBookService {
    private IBookRepository bookRepository;
    private IBookMapper bookMapper;
    private static final Logger logger = LogManager.getLogger(BookService.class);


    @Autowired
    public BookService(IBookRepository bookRepository, IBookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }


    @Override
    @CacheEvict(value = "books", allEntries = true)
    public BookResponse createBook(CreateBookRequest createBookRequest) {
        BookEntity bookEntity = BookEntity.builder()
                .title(createBookRequest.getTitle())
                .author(createBookRequest.getAuthor())
                .description(createBookRequest.getDescription())
                .image(createBookRequest.getImage())
                .stock(createBookRequest.getStock())
                .build();

        BookEntity book = bookRepository.save(bookEntity);

        logger.debug("Book created: {}", book);

        return bookMapper.toBookResponse(book);
    }

    @Override
    @Cacheable(value = "book", key = "'book-' + #id")
    public BookResponse getBookById(Long id) {
        return bookRepository.findById(id)
                .map(b -> {
                    logger.info("Book found: {}", b);
                    return bookMapper.toBookResponse(b);
                })
                .orElseThrow(() -> new CustomException("Book not found", HttpStatus.NOT_FOUND));
    }

    @Override
    @Cacheable(value = "books", key = "'books-page-' + #pageable.pageNumber")
    public Page<BookResponse> getBooks(Pageable pageable) {
        Page<BookResponse> bookResponse = bookRepository.findAll(pageable).map(bookMapper::toBookResponse);
        logger.debug("Books found: {}", new PagedModel<>(bookResponse).getContent());
        return bookResponse;
    }

    @Override
    @CachePut(value = "book", key = "'book-' + #id")
    public BookResponse updateBook(Long id, UpdateBookRequest updateBookRequest) {
        return bookRepository.findById(id).map(b -> {
            Optional.ofNullable(updateBookRequest.getTitle()).ifPresent(b::setTitle);
            Optional.ofNullable(updateBookRequest.getAuthor()).ifPresent(b::setAuthor);
            Optional.ofNullable(updateBookRequest.getDescription()).ifPresent(b::setDescription);
            Optional.ofNullable(updateBookRequest.getImage()).ifPresent(b::setImage);
            Optional.ofNullable(updateBookRequest.getStock()).ifPresent(b::setStock);

            BookEntity bookSaved = bookRepository.save(b);

            logger.debug("Book updated: {}", bookSaved);

            return bookMapper.toBookResponse(bookSaved);
        }).orElseThrow(
                () -> new CustomException("Book not found", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    @CacheEvict(value = "book", key = "'book-' + #id")
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
        logger.debug("Book deleted: {}", id);
    }
}
