package id.my.cupcakez.booktify.domain.book.service;

import id.my.cupcakez.booktify.domain.book.repository.BookQueryFilter;
import id.my.cupcakez.booktify.dto.request.CreateBookRequest;
import id.my.cupcakez.booktify.dto.request.UpdateBookRequest;
import id.my.cupcakez.booktify.dto.response.BookResponse;
import org.springframework.data.domain.Page;

public interface IBookService {
    BookResponse createBook(CreateBookRequest createBookRequest);

    BookResponse findBookById(Long id);

    Page<BookResponse> findBooks(BookQueryFilter bookQueryFilter);

    BookResponse updateBook(Long id, UpdateBookRequest updateBookRequest);

    void deleteBook(Long id);
}
