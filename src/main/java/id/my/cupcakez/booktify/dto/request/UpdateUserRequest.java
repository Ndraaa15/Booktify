package id.my.cupcakez.booktify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest implements Serializable {
    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    @Email(message = "email must be valid")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("confirmPassword")
    private String confirmPassword;

    @JsonProperty("phone")
    @Pattern(regexp = "^\\+62\\d{9,13}$", message = "Phone must start with +62 and contain 9-13 digits")
    private String phone;

    @JsonProperty("address")
    private String address;
}
