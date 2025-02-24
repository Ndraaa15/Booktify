package id.my.cupcakez.booktify.domain.user.repository;

import id.my.cupcakez.booktify.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserQueryFilter {
    private String keyword;
    private UserRole role;
    private Pageable pageable;
}
