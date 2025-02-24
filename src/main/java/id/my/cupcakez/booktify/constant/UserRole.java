package id.my.cupcakez.booktify.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import id.my.cupcakez.booktify.exception.CustomException;
import org.springframework.http.HttpStatus;

public enum UserRole {
    UNKNOWN ("unknown"),
    ADMIN ("admin"),
    STAFF ("staff"),
    USER ("user");

    private final String userRole;

    UserRole(String userRole){
        this.userRole=userRole;
    }

    @JsonValue
    public String getUserRole() {
        return userRole;
    }

    @JsonCreator
    public static UserRole fromValue(String value) {
        for (UserRole userRole : values()) {
            String userRoleType = userRole.getUserRole();
            if (userRoleType.equals(value)) {
                return userRole;
            }
        }
        throw new CustomException(String.format("%s status user role, it's must be either pending, accepted, rejected, on-rent, returned, overdue", UNKNOWN.getUserRole()) + value, HttpStatus.BAD_REQUEST);
    }
}
