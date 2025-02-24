package id.my.cupcakez.booktify.domain.book.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookQueryFilter {
    private String keyword;
    private Pageable pageable;
}
