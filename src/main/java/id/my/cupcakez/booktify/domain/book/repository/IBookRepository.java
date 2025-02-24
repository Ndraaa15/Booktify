package id.my.cupcakez.booktify.domain.book.repository;

import id.my.cupcakez.booktify.entity.BookEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBookRepository extends
        PagingAndSortingRepository<BookEntity, Long>,
        JpaRepository<BookEntity, Long>,
        QuerydslPredicateExecutor<BookEntity> {
    @Query("SELECT b FROM BookEntity b WHERE b.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BookEntity> getBookByIdForUpdate(@Param("id") Long id);

    @Query(value = "SELECT * FROM books b WHERE b.tsv @@ plainto_tsquery(:keyword)", nativeQuery = true)
    Page<BookEntity> findAllByKeywords(@Param("keyword") String keyword, Pageable pageable);
}

