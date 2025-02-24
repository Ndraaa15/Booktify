package id.my.cupcakez.booktify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.my.cupcakez.booktify.constant.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class UpdateUserProfileRequest implements Serializable{
    @JsonProperty("name")
    @Schema(description = "User name", example = "John Doe")
    private String name;

    @JsonProperty("email")
    @Email(message = "email must be valid")
    @Schema(description = "User email", example = "johndoe@gmail.com")
    private String email;

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "User password", example = "password")
    private String password;

    @JsonProperty(value = "confirmPassword", access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "User confirm password", example = "password")
    private String confirmPassword;

    @JsonProperty("phone")
    @Pattern(regexp = "^\\+62\\d{9,13}$", message = "Phone must start with +62 and contain 9-13 digits")
    @Schema(description = "User phone", example = "+6281234567890")
    private String phone;

    @JsonProperty("address")
    @Schema(description = "User address", example = "Jl. Raya Badung")
    private String address;
}
