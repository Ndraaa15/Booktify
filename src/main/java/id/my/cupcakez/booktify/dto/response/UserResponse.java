package id.my.cupcakez.booktify.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import id.my.cupcakez.booktify.constant.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse implements Serializable {
    @JsonProperty("id")
    @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @JsonProperty("name")
    @Schema(description = "User name", example = "Indra Brata")
    private String name;

    @JsonProperty("email")
    @Schema(description = "User email", example = "indrabrata599@gmail.com")
    private String email;

    @JsonProperty("role")
    @Schema(description = "User role", example = "admin")
    private UserRole role;

    @JsonProperty("phone")
    @Schema(description = "User phone", example = "+6281234567890")
    private String phone;

    @JsonProperty("address")
    @Schema(description = "User address", example = "Jl. Raya Kuta No. 1")
    private String address;
}
