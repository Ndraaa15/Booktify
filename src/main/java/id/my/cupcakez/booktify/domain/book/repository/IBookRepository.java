package id.my.cupcakez.booktify.domain.book.repository;

import com.querydsl.core.types.dsl.StringPath;
import id.my.cupcakez.booktify.entity.BookEntity;
import id.my.cupcakez.booktify.entity.QBookEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBookRepository extends
        JpaRepository<BookEntity, Long>,
        PagingAndSortingRepository<BookEntity, Long>,
        QuerydslPredicateExecutor<BookEntity>,
        QuerydslBinderCustomizer<QBookEntity>
{
    @Query("SELECT b FROM BookEntity b WHERE b.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BookEntity> getBookByIdForUpdate(Long id);


    @Override
    default void customize(QuerydslBindings bindings, QBookEntity book) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
