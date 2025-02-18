package id.my.cupcakez.booktify.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserRole role) {
        if (role == null) {
            return null;
        }
        return role.ordinal();
    }

    @Override
    public UserRole convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Stream.of(UserRole.values())
                .filter(role -> role.ordinal() == dbData)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

