package id.my.cupcakez.booktify.domain.book.controller;

import id.my.cupcakez.booktify.domain.book.repository.BookQueryFilter;
import id.my.cupcakez.booktify.domain.book.service.IBookService;
import id.my.cupcakez.booktify.dto.request.CreateBookRequest;
import id.my.cupcakez.booktify.dto.request.UpdateBookRequest;
import id.my.cupcakez.booktify.dto.response.BookResponse;
import id.my.cupcakez.booktify.response.ResponseWrapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;

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
    public ResponseEntity<ResponseWrapper<BookResponse>> createBook(
            @Validated
            @RequestBody
            CreateBookRequest createBookRequest
    ) {
        BookResponse bookResponse = bookService.createBook(createBookRequest);
        ResponseWrapper<BookResponse> response = ResponseWrapper.<BookResponse>builder()
                .message("Book created successfully")
                .data(bookResponse)
                .build();
        return ResponseEntity.created(URI.create("/api/v1/books")).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<BookResponse>> findBookById(
            @PathVariable("id") Long id
    ) {
        BookResponse bookResponse = bookService.findBookById(id);
        ResponseWrapper<BookResponse> response = ResponseWrapper.<BookResponse>builder()
                .message("Book retrieved successfully")
                .data(bookResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<ResponseWrapper<PagedModel<BookResponse>>> findBooks(
            @RequestParam(value = "keyword", required = false, defaultValue = "")
            String keyword,
            @ParameterObject
            @PageableDefault(sort = "created_at", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        String keywordFormatted = Arrays.stream(keyword.split(" ")).map(String::toLowerCase).reduce((a, b) -> a + "|" + b).orElse("");
        BookQueryFilter bookQueryFilter = BookQueryFilter.builder()
                .keyword(keywordFormatted)
                .pageable(pageable)
                .build();
        Page<BookResponse> books = bookService.findBooks(bookQueryFilter);
        ResponseWrapper<PagedModel<BookResponse>> response = ResponseWrapper.<PagedModel<BookResponse>>builder()
                .message("books retrieved successfully")
                .data(new PagedModel<>(books)).build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
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
    public ResponseEntity<?> deleteBook(
            @PathVariable("id") Long id
    ) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
