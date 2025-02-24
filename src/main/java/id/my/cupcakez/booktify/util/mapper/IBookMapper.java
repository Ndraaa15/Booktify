package id.my.cupcakez.booktify.util.mapper;

import id.my.cupcakez.booktify.dto.response.BookResponse;
import id.my.cupcakez.booktify.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IBookMapper {
    @Named("toBookResponse")
    BookResponse toBookResponse(BookEntity bookEntity);
}
