package id.my.cupcakez.booktify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "User name", example = "Indra Brata")
    private String name;

    @JsonProperty("email")
    @Email(message = "email must be valid")
    @NotBlank(message = "email must not be blank")
    @Schema(description = "User email", example = "indrabrata599@gmail.com")
    private String email;

    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "password must not be blank")
    @Schema(description = "User password", example = "IndraSuper123!")
    private String password;

    @JsonProperty(value = "confirmPassword", access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "confirm Password must not be blank")
    @Schema(description = "User confirm password", example = "IndraSuper123!")
    private String confirmPassword;

    @JsonProperty("phone")
    @Pattern(regexp = "^\\+62\\d{9,13}$", message = "Phone must start with +62 and contain 9-13 digits")
    @NotBlank(message = "phone must not be blank")
    @Schema(description = "User phone", example = "+6281234567890")
    private String phone;

    @JsonProperty("address")
    @NotBlank(message = "address must not be blank")
    @Schema(description = "User address", example = "Jl. Raya Kuta No. 1")
    private String address;
}
