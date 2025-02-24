package id.my.cupcakez.booktify.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import id.my.cupcakez.booktify.constant.UserRole;
import id.my.cupcakez.booktify.constant.UserRoleConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone")
        }, indexes = {
        @Index(columnList = "email"),
        @Index(columnList = "id")
})
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, name = "id")
    private UUID id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Convert(converter = UserRoleConverter.class)
    @Column(nullable = false, name = "role")
    private UserRole role;

    @Column(nullable = false, name = "address")
    private String address;

    @Column(nullable = false, unique = true, name = "phone")
    private String phone;

    @CreationTimestamp
    @Column(updatable = false, nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;
}