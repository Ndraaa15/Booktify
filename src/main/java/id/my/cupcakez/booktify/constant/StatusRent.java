package id.my.cupcakez.booktify.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import id.my.cupcakez.booktify.exception.CustomException;
import org.springframework.http.HttpStatus;

public enum StatusRent {
    PENDING ("pending"),
    ACCEPTED ("accepted"),
    REJECTED ("rejected"),
    ON_RENT ("on-rent"),
    RETURNED ("returned");

    private final String statusRent;

    StatusRent(String statusRent){
        this.statusRent = statusRent;
    }

    @JsonValue
    public String getStatusRentType() {
        return statusRent;
    }

    @JsonCreator
    public static StatusRent fromValue(String value) {
        for (StatusRent statusRent : values()) {
            String statusRentType = statusRent.getStatusRentType();
            if (statusRentType.equals(value)) {
                return statusRent;
            }
        }

        throw new CustomException("Invalid value for StatusRent type Enum: " + value,  HttpStatus.BAD_REQUEST);
    }
}
