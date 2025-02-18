package id.my.cupcakez.booktify.domain.book.repository;

import id.my.cupcakez.booktify.entity.BookEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
    public interface IBookRepository extends JpaRepository<BookEntity, Long>, PagingAndSortingRepository<BookEntity, Long> {
    @Query("SELECT b FROM BookEntity b WHERE b.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BookEntity> getBookByIdForUpdate(Long id);
}
