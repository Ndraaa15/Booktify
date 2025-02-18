package id.my.cupcakez.booktify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CreateRentRequest implements Serializable {
    @JsonProperty("bookId")
    @NotBlank(message = "bookId must not be blank")
    private Long bookId;

    @JsonProperty("quantity")
    @NotBlank(message = "quantity must not be blank")
    private Integer quantity;

    @JsonProperty("rentedUntil")
    @NotBlank(message = "rentedUntil must not be blank")
    private String rentedUntil;
}
