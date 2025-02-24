package id.my.cupcakez.booktify.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.my.cupcakez.booktify.constant.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    @JsonProperty("email")
    @Schema(description = "User email", example = "indrabrata599@gmail.com")
    private String email;

    @JsonProperty("role")
    @Schema(description = "User role", example = "user")
    private UserRole role;

    @JsonProperty("token")
    @Schema(description = "User token", example = "xxxxxxx")
    private String token;
}
