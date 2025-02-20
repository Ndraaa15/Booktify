package id.my.cupcakez.booktify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequest implements Serializable {
    @JsonProperty("email")
    @Email(message = "email must be valid")
    @NotBlank(message = "email must not be blank")
    @Schema(description = "User email", example = "indrabrata599@gmail.com")
    private String email;

    @JsonProperty("password")
    @NotBlank(message = "password must not be blank")
    @Schema(description = "User password", example = "IndraSuper123!")
    private String password;
}
