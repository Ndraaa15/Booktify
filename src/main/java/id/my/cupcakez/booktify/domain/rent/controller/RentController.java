package id.my.cupcakez.booktify.domain.rent.controller;

import id.my.cupcakez.booktify.domain.rent.service.IRentService;
import id.my.cupcakez.booktify.dto.request.CreateRentRequest;
import id.my.cupcakez.booktify.dto.request.UpdateRentRequest;
import id.my.cupcakez.booktify.dto.response.RentResponse;
import id.my.cupcakez.booktify.response.ResponseWrapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
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
    public ResponseEntity<ResponseWrapper<RentResponse>> createRent(
            @Validated
            @RequestBody
            CreateRentRequest createRentRequest
    ){
        UUID userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        RentResponse rentResponse = rentService.createRent(userId, createRentRequest);
        ResponseWrapper<RentResponse> response = new ResponseWrapper<>(
                "Rent created successfully",
                rentResponse
        );
        return ResponseEntity.created(URI.create("/api/v1/rents")).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<RentResponse>> getRentById(
            @PathVariable("id")
            Long id
    ){
        RentResponse rentResponse = rentService.getRentById(id);
        ResponseWrapper<RentResponse> response = new ResponseWrapper<>(
                "Rent found successfully",
                rentResponse
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseWrapper<RentResponse>> updateRent(
            @PathVariable("id")
            Long id,
            @Validated
            @RequestBody
            UpdateRentRequest updateRentRequest
    ){
        RentResponse rentResponse = rentService.updateRent(id, updateRentRequest);
        ResponseWrapper<RentResponse> response = new ResponseWrapper<>(
                "Rent updated successfully",
                rentResponse
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<ResponseWrapper<PagedModel<RentResponse>>> getRent(
            @ParameterObject
            @PageableDefault(sort = "created_at", direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        Page<RentResponse> rents = rentService.getRents(pageable);
        ResponseWrapper<PagedModel<RentResponse>> response = ResponseWrapper.<PagedModel<RentResponse>>builder()
                .message("rents retrieved successfully")
                .data(new PagedModel<>(rents))
                .build();
        return ResponseEntity.ok(response);
    }
}
