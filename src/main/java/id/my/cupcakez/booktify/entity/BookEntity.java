package id.my.cupcakez.booktify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import java.io.Serializable;
import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table( name = "books")
public class BookEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_seq")
    @SequenceGenerator(name = "book_id_seq", sequenceName = "book_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Integer stock;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "tsv", insertable = false, updatable = false, columnDefinition = "tsvector")
    private String bookFts;

}