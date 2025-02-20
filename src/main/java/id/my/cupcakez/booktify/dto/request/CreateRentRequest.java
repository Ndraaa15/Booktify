package id.my.cupcakez.booktify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "bookId must not be null")
    @Min(value = 1, message = "bookId must be greater than 0")
    private Long bookId;

    @JsonProperty("quantity")
    @NotNull(message = "quantity must not be null")
    @Min(value = 1, message = "quantity must be greater than 0")
    private Integer quantity;

    @JsonProperty("rentedUntil")
    @NotBlank(message = "rentedUntil must not be blank")
    private String rentedUntil;
}
