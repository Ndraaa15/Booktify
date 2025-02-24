package id.my.cupcakez.booktify.constant;

import jakarta.persistence.AttributeConverter;

import java.util.stream.Stream;

public class StatusRentConverter implements AttributeConverter<StatusRent, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StatusRent status) {
        return status.ordinal();
    }

    @Override
    public StatusRent convertToEntityAttribute(Integer dbData) {
        return Stream.of(StatusRent.values())
                .filter(role -> role.ordinal() == dbData)
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException(String.format("%d unknown status rent, failed to convert into status rent", dbData))
                );
    }
}
