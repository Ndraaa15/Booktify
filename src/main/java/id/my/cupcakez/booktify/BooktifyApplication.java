package id.my.cupcakez.booktify;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@OpenAPIDefinition(
		info = @Info(
				title = "Booktify",
				description = "A simple api for library",
				version = "v1",
				license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0")
		)
)
@EnableCaching
@EnableScheduling
public class BooktifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooktifyApplication.class, args);
	}

}
