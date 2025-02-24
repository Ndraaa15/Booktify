package id.my.cupcakez.booktify.domain.book.controller;

import id.my.cupcakez.booktify.domain.book.repository.IBookRepository;
import id.my.cupcakez.booktify.domain.book.service.IBookService;
import id.my.cupcakez.booktify.dto.request.CreateBookRequest;
import id.my.cupcakez.booktify.dto.request.UpdateBookRequest;
import id.my.cupcakez.booktify.dto.response.BookResponse;
import id.my.cupcakez.booktify.entity.BookEntity;
import id.my.cupcakez.booktify.response.ResponseWrapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.Objects;
import com.querydsl.core.types.Predicate;

@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "books", description = "Books API")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class BookController {
    private IBookService bookService;

    @Autowired
    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyAuthority('STAFF', 'ADMIN')")
    public ResponseEntity<ResponseWrapper<BookResponse>> createBook(
            @Validated
            @RequestBody
            CreateBookRequest createBookRequest
    ) {
        BookResponse bookResponse = bookService.createBook(createBookRequest);
        ResponseWrapper<BookResponse> response = ResponseWrapper.<BookResponse>builder()
                .message("book created successfully")
                .data(bookResponse)
                .build();
        return ResponseEntity.created(URI.create("/api/v1/books")).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF', 'USER', 'ADMIN')")
    public ResponseEntity<ResponseWrapper<BookResponse>> getBook(
            @PathVariable("id") Long id
    ) {
        BookResponse bookResponse = bookService.getBookById(id);
        ResponseWrapper<BookResponse> response = ResponseWrapper.<BookResponse>builder()
                .message("book retrieved successfully")
                .data(bookResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<ResponseWrapper<PagedModel<BookResponse>>> getBooks(
            @RequestParam(value = "keyword", required = false, defaultValue = "")
            String keyword,
            @ParameterObject
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Page<BookResponse> books = bookService.getBooks(keyword, pageable);
        ResponseWrapper<PagedModel<BookResponse>> response = ResponseWrapper.<PagedModel<BookResponse>>builder()
                .message("books retrieved successfully")
                .data(new PagedModel<>(books)).build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<ResponseWrapper<BookResponse>> updateBook(
            @PathVariable("id") Long id,
            @Validated
            @RequestBody
            UpdateBookRequest updateBookRequest
    ) {
        BookResponse bookResponse = bookService.updateBook(id, updateBookRequest);
        ResponseWrapper<BookResponse> response = ResponseWrapper.<BookResponse>builder()
                .message("book updated successfully")
                .data(bookResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    public ResponseEntity<?> deleteBook(
            @PathVariable("id") Long id
    ) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
