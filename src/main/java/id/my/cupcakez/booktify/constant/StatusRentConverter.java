package id.my.cupcakez.booktify.constant;

import jakarta.persistence.AttributeConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class StatusRentConverter implements AttributeConverter<StatusRent, Integer>, Converter<String, StatusRent> {

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

    @Override
    public StatusRent convert(String source) {
        return StatusRent.fromValue(source);
    }
}
