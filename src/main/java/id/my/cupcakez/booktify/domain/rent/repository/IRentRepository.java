package id.my.cupcakez.booktify.domain.rent.repository;

import id.my.cupcakez.booktify.entity.RentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRentRepository extends JpaRepository<RentEntity, Long> {
}
