package id.my.cupcakez.booktify.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import id.my.cupcakez.booktify.exception.CustomException;
import org.springframework.http.HttpStatus;

public enum UserRole {
    ADMIN ("admin"),
    STAFF ("staff"),
    USER ("user");

    private final String userRole;

    UserRole(String userRole){
        this.userRole=userRole;
    }

    @JsonValue
    public String getUserRoleType() {
        return userRole;
    }

    @JsonCreator
    public static UserRole fromValue(String value) {
        for (UserRole userRole : values()) {
            String userRoleType = userRole.getUserRoleType();
            if (userRoleType.equals(value)) {
                return userRole;
            }
        }

        throw new CustomException("Invalid value for UserRole type Enum: " + value, HttpStatus.BAD_REQUEST);
    }
}
