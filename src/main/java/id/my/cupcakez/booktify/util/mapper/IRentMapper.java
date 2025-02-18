package id.my.cupcakez.booktify.util.mapper;

import id.my.cupcakez.booktify.dto.response.RentResponse;
import id.my.cupcakez.booktify.entity.RentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IRentMapper {
    RentResponse toRentResponse(RentEntity rentEntity);
}
