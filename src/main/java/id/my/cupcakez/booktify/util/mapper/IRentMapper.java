package id.my.cupcakez.booktify.util.mapper;

import id.my.cupcakez.booktify.dto.response.BookResponse;
import id.my.cupcakez.booktify.dto.response.RentResponse;
import id.my.cupcakez.booktify.entity.BookEntity;
import id.my.cupcakez.booktify.entity.RentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IRentMapper {
    @Named("toRentResponse")
    RentResponse toRentResponse(RentEntity rentEntity);

    @Named("toRentResponseWithoutUser")
    @Mapping(target = "user", source = "user",ignore = true)
    RentResponse toRentResponseWithoutUser(RentEntity rentEntity);
}
