package id.my.cupcakez.booktify.domain.rent.repository;

import id.my.cupcakez.booktify.dto.response.RentResponse;
import id.my.cupcakez.booktify.entity.RentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface IRentRepository extends JpaRepository<RentEntity, Long> {
    @Query("SELECT r FROM RentEntity r WHERE r.user.id = :userId")
    List<RentEntity> getRentByUserId(UUID userId);
}
