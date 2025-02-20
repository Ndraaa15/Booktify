package id.my.cupcakez.booktify.util.mapper;

import id.my.cupcakez.booktify.dto.response.UserResponse;
import id.my.cupcakez.booktify.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    UserResponse toUserResponse(UserEntity userEntity);
}