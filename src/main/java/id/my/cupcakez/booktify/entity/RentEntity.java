package id.my.cupcakez.booktify.entity;

import id.my.cupcakez.booktify.constant.StatusRent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rents")
public class RentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rent_id_seq")
    @SequenceGenerator(name = "rent_id_seq", sequenceName = "rent_id_seq", allocationSize = 1)
    private Long id;

    @JoinColumn(name = "user_id", referencedColumnName = "id", table = "users")
    private UUID userId;

    @JoinColumn(name = "book_id", referencedColumnName = "id", table = "books")
    private Long bookId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private StatusRent status;

    @Column(nullable = false)
    private LocalDate rentedUntil;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
