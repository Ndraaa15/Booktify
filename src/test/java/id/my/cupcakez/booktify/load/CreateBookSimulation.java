package id.my.cupcakez.booktify.load;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import id.my.cupcakez.booktify.dto.request.UserLoginRequest;
import id.my.cupcakez.booktify.dto.response.LoginResponse;
import id.my.cupcakez.booktify.response.ResponseWrapper;
import io.gatling.javaapi.core.*;


import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import io.gatling.javaapi.core.OpenInjectionStep.RampRate.RampRateOpenInjectionStep;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;



public class CreateBookSimulation extends Simulation {
    @Autowired
    private ObjectMapper objectMapper;

    public CreateBookSimulation() throws JsonProcessingException {
        RestClient client = RestClient.builder().baseUrl("http://localhost:8080").build();
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email("admin@example.com")
                .password("admin123")
                .build();
        ResponseSpec response = client.post().uri("/api/v1/auth/login")
                .header("Content-Type", "application/json")
                .body(userLoginRequest)
                .retrieve();

        ResponseWrapper<LoginResponse> responseEntity = response.body(new ParameterizedTypeReference<ResponseWrapper<LoginResponse>>() {});
        assert responseEntity != null;
        LoginResponse loginResponse = (LoginResponse) responseEntity.getData();

        setUp(buildPostScenario(loginResponse.getToken())
                .injectOpen(atOnceUsers(100))
                .protocols(setupProtocol())).assertions(global().responseTime()
                .max()
                .lte(10000), global().successfulRequests()
                .percent()
                .gt(90d));
    }


    private static HttpProtocolBuilder setupProtocol() {
        return HttpDsl.http.baseUrl("http://localhost:8080")
                .acceptHeader("application/json")
                .maxConnectionsPerHost(10)
                .userAgentHeader("Performance Test");
    }



    private static ScenarioBuilder buildPostScenario(String token) {
        return CoreDsl.scenario("Load Test Create Book")
                .feed(feedData())
                .exec(http("create-book").post("/api/v1/books")
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .body(StringBody("{ " +
                                "\"title\": \"#{title}\", " +
                                "\"description\": \"#{description}\", " +
                                "\"author\": \"#{author}\", " +
                                "\"image\": \"#{image}\", " +
                                "\"stock\": #{stock} }"
                        )
                ).check(status().is(201)));
    }

    private static Iterator <Map<String, Object>> feedData() {
        Faker faker = new Faker();
        Iterator<Map<String, Object>> iterator;
        iterator = Stream.generate(() -> {
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("title", faker.book().title());
            stringObjectMap.put("author", faker.book().author());
            stringObjectMap.put("description", faker.lorem().sentence());
            stringObjectMap.put("image", faker.internet().image());
            stringObjectMap.put("stock", faker.number().numberBetween(1, 100));
            return stringObjectMap;
        }).iterator();
        return iterator;
    }

    private RampRateOpenInjectionStep injection() {
        int totalUsers = 100;
        double userRampUpPerInterval = 10;
        double rampUpIntervalInSeconds = 30;

        int rampUptimeSeconds = 300;
        int duration = 300;
        return rampUsersPerSec(userRampUpPerInterval / (rampUpIntervalInSeconds))
                .to(totalUsers)
                .during(Duration.ofSeconds(rampUptimeSeconds + duration));
    }
}
