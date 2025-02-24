package id.my.cupcakez.booktify.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import id.my.cupcakez.booktify.constant.StatusRent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRentResponse implements Serializable {
    @JsonProperty("id")
    @Schema(description = "Rent ID", example = "1")
    private Long id;

    @JsonProperty("book")
    @Schema(description = "Book data")
    private BookResponse book;

    @JsonProperty("quantity")
    @Schema(description = "Quantity of book rented", example = "1")
    private Integer quantity;

    @JsonProperty("rentedUntil")
    @JsonFormat(shape = JsonFormat.Shape.STRING ,pattern = "dd-MM-yyyy")
    @Schema(description = "Rented until date", example = "01-01-2022")
    private LocalDate rentedUntil;

    @JsonProperty("status")
    @Schema(description = "Rent status", example = "rented")
    private StatusRent status;
}
