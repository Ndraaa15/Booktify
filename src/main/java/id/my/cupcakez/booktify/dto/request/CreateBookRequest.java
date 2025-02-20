package id.my.cupcakez.booktify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Book title", example = "The Great Gatsby")
    private String title;

    @JsonProperty("author")
    @NotBlank(message = "author must not be blank")
    @Schema(description = "Book author", example = "F. Scott Fitzgerald")
    private String author;

    @JsonProperty("description")
    @Schema(description = "Book description", example = "The Great Gatsby is a novel by F. Scott Fitzgerald that was first published in 1925.")
    @NotBlank(message = "description must not be blank")
    private String description;

    @JsonProperty("image")
    @Schema(description = "Book image", example = "https://example.com/image.jpg")
    @NotBlank(message = "image must not be blank")
    private String image;

    @JsonProperty("stock")
    @Schema(description = "Book stock", example = "10")
    @NotNull(message = "stock must not be null")
    private Integer stock;
}
