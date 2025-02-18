package id.my.cupcakez.booktify.constant;

import jakarta.persistence.AttributeConverter;

import java.util.stream.Stream;

public class StatusRentConverter implements AttributeConverter<StatusRent, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StatusRent status) {
        if (status == null) {
            return null;
        }
        return status.ordinal();
    }

    @Override
    public StatusRent convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        return Stream.of(StatusRent.values())
                .filter(role -> role.ordinal() == dbData)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
