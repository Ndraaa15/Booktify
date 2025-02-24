package id.my.cupcakez.booktify.constant;

import id.my.cupcakez.booktify.exception.CustomException;
import jakarta.persistence.AttributeConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class UserRoleConverter implements AttributeConverter<UserRole, Integer>, Converter<String, UserRole> {
    @Override
    public Integer convertToDatabaseColumn(UserRole role) {
        return role.ordinal();
    }

    @Override
    public UserRole convertToEntityAttribute(Integer dbData) {
        return Stream.of(UserRole.values())
                .filter(role -> role.ordinal() == dbData)
                .findFirst()
                .orElseThrow(
                        () -> new CustomException(String.format("%d unknown user role, failed to convert into user role", dbData), HttpStatus.BAD_REQUEST)
                );
    }

    @Override
    public UserRole convert(String source) {
        return UserRole.fromValue(source);
    }
}

