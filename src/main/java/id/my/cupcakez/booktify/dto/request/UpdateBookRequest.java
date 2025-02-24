package id.my.cupcakez.booktify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookRequest implements Serializable {
    @JsonProperty("title")
    @Schema(description = "Book title", example = "Atomic Habit")
    private String title;

    @JsonProperty("author")
    @Schema(description = "Book author", example = "James Clear")
    private String author;

    @JsonProperty("description")
    @Schema(description = "Book description", example = "Atomic Habits is a book by James Clear that talks about how to build good habits and break bad ones.")
    private String description;

    @JsonProperty("image")
    @URL
    @Schema(description = "Book image", example = "https://example.com/image.jpg")
    private String image;

    @JsonProperty("stock")
    @Schema(description = "Book stock", example = "10")
    private Integer stock;
}
