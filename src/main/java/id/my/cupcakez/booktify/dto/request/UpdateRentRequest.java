package id.my.cupcakez.booktify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.my.cupcakez.booktify.constant.StatusRent;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRentRequest implements Serializable {
    @JsonProperty("status")
    @Schema(description = "Rent status", example = "returned")
    private StatusRent status;
}
