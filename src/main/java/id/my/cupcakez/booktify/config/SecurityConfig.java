package id.my.cupcakez.booktify.config;

import id.my.cupcakez.booktify.security.filter.JwtFilter;
import id.my.cupcakez.booktify.util.jwt.IJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtFilter jwtFilter) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").anonymous()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").anonymous()
                        .requestMatchers(HttpMethod.GET, "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/books/**").hasAnyAuthority("USER", "STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/v1/books").hasAnyAuthority("STAFF")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/books/**").hasAnyAuthority("STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/books/**").hasAnyAuthority("STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/v1/rents/**").hasAnyAuthority("STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/v1/rents").hasAnyAuthority("USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/rents/**").hasAnyAuthority("STAFF")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/me").hasAnyAuthority("USER", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/me").hasAnyAuthority("USER", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/me/rents").hasAnyAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyAuthority("STAFF")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/**").hasAnyAuthority("STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAnyAuthority("STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/v1/files/upload").hasAnyAuthority("STAFF")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}