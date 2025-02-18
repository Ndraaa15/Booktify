package id.my.cupcakez.booktify.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RentResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("userId")
    private UUID userId;

    @JsonProperty("bookId")
    private Long bookId;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("rentedUntil")
    private String rentedUntil;

    @JsonProperty("status")
    private String status;
}
