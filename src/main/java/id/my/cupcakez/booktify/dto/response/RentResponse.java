package id.my.cupcakez.booktify.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class RentResponse implements Serializable {
    @JsonProperty("id")
    private Long id;

    @JsonProperty(value = "userId", access = JsonProperty.Access.WRITE_ONLY)
    private UUID userId;

    @JsonProperty(value = "bookId", access = JsonProperty.Access.WRITE_ONLY)
    private Long bookId;

    @JsonProperty("user")
    private UserResponse user;

    @JsonProperty("book")
    private BookResponse book;

    @JsonProperty("quantity")
    private Integer quantity;

    @JsonProperty("rentedUntil")
    @JsonFormat(shape = JsonFormat.Shape.STRING ,pattern = "dd-MM-yyyy")
    private LocalDate rentedUntil;

    @JsonProperty("status")
    private String status;
}
