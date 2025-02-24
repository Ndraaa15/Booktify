package id.my.cupcakez.booktify.util.mapper;

import id.my.cupcakez.booktify.dto.response.UserRentResponse;
import id.my.cupcakez.booktify.dto.response.UserResponse;
import id.my.cupcakez.booktify.entity.RentEntity;
import id.my.cupcakez.booktify.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    @Named("toUserResponse")
    UserResponse toUserResponse(UserEntity userEntity);

    @Named("toUserRentResponse")
    UserRentResponse toUserRentResponse(RentEntity userEntity);
}