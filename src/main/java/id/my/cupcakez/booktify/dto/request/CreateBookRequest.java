package id.my.cupcakez.booktify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CreateBookRequest implements Serializable {
    @JsonProperty("title")
    @NotBlank(message = "title must not be blank")
    private String title;

    @JsonProperty("author")
    @NotBlank(message = "author must not be blank")
    private String author;

    @JsonProperty("description")
    @NotBlank(message = "description must not be blank")
    private String description;

    @JsonProperty("image")
    @NotBlank(message = "image must not be blank")
    private String image;

    @JsonProperty("stock")
    @NotNull(message = "stock must not be null")
    private Integer stock;
}
