package id.my.cupcakez.booktify.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseWrapper <T> {
    @JsonProperty("message")
    @Schema(description = "Response message", example = "any message that related to operation")
    String message;

    @JsonProperty("data")
    @Schema(description = "Response data")
    T data;
}
