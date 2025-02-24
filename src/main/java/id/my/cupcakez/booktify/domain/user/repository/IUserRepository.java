package id.my.cupcakez.booktify.domain.user.repository;

import aj.org.objectweb.asm.commons.Remapper;
import id.my.cupcakez.booktify.entity.RentEntity;
import id.my.cupcakez.booktify.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("SELECT r FROM RentEntity r WHERE r.user.id = :userId")
    Page<RentEntity> findRentsByUserId(UUID userId, Pageable pageable);
}
