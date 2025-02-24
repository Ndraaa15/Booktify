package id.my.cupcakez.booktify.domain.rent.repository;

import aj.org.objectweb.asm.commons.Remapper;
import id.my.cupcakez.booktify.constant.StatusRent;
import id.my.cupcakez.booktify.dto.response.RentResponse;
import id.my.cupcakez.booktify.entity.BookEntity;
import id.my.cupcakez.booktify.entity.RentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IRentRepository extends JpaRepository<RentEntity, Long> {
    @Modifying
    @Query("UPDATE RentEntity r SET r.status = :overdue WHERE r.status = :onRent AND r.rentedUntil <= :now")
    @Transactional
    Integer updateOverdueRent(LocalDate now, StatusRent onRent, StatusRent overdue);

    @Query("SELECT r FROM RentEntity r WHERE (LOWER(r.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.book.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<RentEntity> findAll(@Param("keyword") String keyword, @Param("status") StatusRent statusRent, Pageable pageable);

    @Query("SELECT r FROM RentEntity r WHERE (LOWER(r.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.book.title) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<RentEntity> findAll(String keyword, Pageable pageable);
}
