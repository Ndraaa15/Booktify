package id.my.cupcakez.booktify.domain.rent.controller;

import id.my.cupcakez.booktify.domain.rent.service.IRentService;
import id.my.cupcakez.booktify.dto.request.CreateRentRequest;
import id.my.cupcakez.booktify.dto.request.UpdateRentRequest;
import id.my.cupcakez.booktify.dto.response.RentResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.SecurityMarker;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rents")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "rents", description = "Rents API")
public class RentController {
    private IRentService rentService;

    @Autowired
    public RentController(IRentService rentService){
        this.rentService = rentService;
    }


    @PostMapping("")
    public ResponseEntity<?> createRent(@Validated @RequestBody CreateRentRequest createRentRequest){
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        RentResponse rentResponse = rentService.createRent(userId, createRentRequest);

        Map<String, Object> response = Map.of(
                "message", "Rent created successfully",
                "rent", rentResponse
        );

        return ResponseEntity.created(URI.create("/api/v1/rents")).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRentById(@PathVariable Long id){

        RentResponse rentResponse = rentService.getRentById(id);

        Map<String, Object> response = Map.of(
                "message", "Rent returned successfully",
                "rent", rentResponse
        );


        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateRent(@Validated @RequestBody UpdateRentRequest updateRentRequest){
        return ResponseEntity.ok("Rent retrieved successfully");
    }

    @GetMapping("")
    public ResponseEntity<?> getRent(Pageable pageable){
        Page<RentResponse> rents = rentService.getRents(pageable);

        Map<String, Object> response = Map.of(
                "message", ""
        );

        return ResponseEntity.ok("Rent retrieved successfully");
    }
}
