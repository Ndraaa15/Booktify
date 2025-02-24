package id.my.cupcakez.booktify.domain.rent.repository;

import id.my.cupcakez.booktify.constant.StatusRent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentQueryFilter {
    private String keyword;
    private StatusRent statusRent;
    private Pageable pageable;
}
