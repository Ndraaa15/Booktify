package id.my.cupcakez.booktify.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookResponse implements Serializable {
    @JsonProperty("id")
    @Schema(description = "Book ID", example = "1")
    private Long id;

    @JsonProperty("title")
    @Schema(description = "Book Title", example = "The Great Gatsby")
    private String title;

    @JsonProperty("author")
    @Schema(description = "Book Author", example = "F. Scott Fitzgerald")
    private String author;

    @JsonProperty("description")
    @Schema(description = "Book Description", example = "The Great Gatsby is a novel by American writer F. Scott Fitzgerald.")
    private String description;

    @JsonProperty("image")
    @Schema(description = "Book Image", example = "https://example.com/image.jpg")
    private String image;

    @JsonProperty("stock")
    @Schema(description = "Book Stock", example = "10")
    private Integer stock;
}
