package id.my.cupcakez.booktify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest implements Serializable {
    @JsonProperty("name")
    @NotBlank(message = "name must not be blank")
    private String name;

    @JsonProperty("email")
    @Email(message = "email must be valid")
    @NotBlank(message = "email must not be blank")
    private String email;

    @JsonProperty("password")
    @NotBlank(message = "password must not be blank")
    private String password;

    @JsonProperty("confirmPassword")
    @NotBlank(message = "confirm Password must not be blank")
    private String confirmPassword;

    @JsonProperty("phone")
    @Pattern(regexp = "^\\+62\\d{9,13}$", message = "Phone must start with +62 and contain 9-13 digits")
    @NotBlank(message = "phone must not be blank")
    private String phone;

    @JsonProperty("address")
    @NotBlank(message = "address must not be blank")
    private String address;
}
