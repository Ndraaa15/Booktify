package id.my.cupcakez.booktify.domain.book.service;

import id.my.cupcakez.booktify.dto.request.CreateBookRequest;
import id.my.cupcakez.booktify.dto.request.UpdateBookRequest;
import id.my.cupcakez.booktify.dto.request.UpdateRentRequest;
import id.my.cupcakez.booktify.dto.response.BookResponse;
import id.my.cupcakez.booktify.dto.response.RentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookService {
    BookResponse createBook(CreateBookRequest createBookRequest);

    BookResponse getBookById(Long id);

    Page<BookResponse> getBooks(Pageable pageable);

    BookResponse updateBook(Long id, UpdateBookRequest updateBookRequest);

    void deleteBook(Long id);
}
