package id.my.cupcakez.booktify.domain.rent.service;

import id.my.cupcakez.booktify.dto.request.CreateRentRequest;
import id.my.cupcakez.booktify.dto.request.UpdateRentRequest;
import id.my.cupcakez.booktify.dto.response.RentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IRentService {
    RentResponse createRent(UUID userId, CreateRentRequest createRentRequest);

    RentResponse getRentById(Long id);

    Page<RentResponse> getRents(Pageable pageable);

    RentResponse updateRent(Long id, UpdateRentRequest updateRentRequest);
}
