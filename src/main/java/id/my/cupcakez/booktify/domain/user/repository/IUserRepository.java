package id.my.cupcakez.booktify.domain.user.repository;

import aj.org.objectweb.asm.commons.Remapper;
import id.my.cupcakez.booktify.constant.StatusRent;
import id.my.cupcakez.booktify.constant.UserRole;
import id.my.cupcakez.booktify.entity.RentEntity;
import id.my.cupcakez.booktify.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("SELECT u FROM UserEntity u WHERE u.email LIKE %:keyword% OR u.name LIKE %:keyword% AND u.role = :role")
    Page<UserEntity> findAll(@Param("keyword") String keyword, @Param("role") UserRole userRole, Pageable pageable);

    @Query("SELECT u FROM UserEntity u WHERE u.email LIKE %:keyword% OR u.name LIKE %:keyword%")
    Page<UserEntity> findAll(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT r FROM RentEntity r WHERE (LOWER(r.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.book.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:status IS NULL OR r.status = :status)" +
            "AND r.user.id = :userId")
    Page<RentEntity> findRentsByUserId(@Param("userId") UUID userId, @Param("keyword") String keyword, @Param("status") StatusRent statusRent, Pageable pageable);

    @Query("SELECT r FROM RentEntity r WHERE (LOWER(r.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.book.title) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
            "AND r.user.id = :userId")
    Page<RentEntity> findRentsByUserId(@Param("userId") UUID userId, @Param("keyword") String keyword, Pageable pageable);

}
