package id.my.cupcakez.booktify.domain.rent.repository;

import aj.org.objectweb.asm.commons.Remapper;
import id.my.cupcakez.booktify.constant.StatusRent;
import id.my.cupcakez.booktify.dto.response.RentResponse;
import id.my.cupcakez.booktify.entity.BookEntity;
import id.my.cupcakez.booktify.entity.RentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IRentRepository extends JpaRepository<RentEntity, Long> {
    @Modifying
    @Query("UPDATE RentEntity r SET r.status = :overdue WHERE r.status = :onRent AND r.rentedUntil <= :now")
    @Transactional
    Integer updateOverdueRent(LocalDate now, StatusRent onRent, StatusRent overdue);
}
