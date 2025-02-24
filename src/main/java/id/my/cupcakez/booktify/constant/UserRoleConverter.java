package id.my.cupcakez.booktify.constant;

import id.my.cupcakez.booktify.exception.CustomException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, Integer> {
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
}

