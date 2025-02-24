package id.my.cupcakez.booktify.domain.rent.service;

import id.my.cupcakez.booktify.domain.rent.repository.RentQueryFilter;
import id.my.cupcakez.booktify.dto.request.CreateRentRequest;
import id.my.cupcakez.booktify.dto.request.UpdateRentRequest;
import id.my.cupcakez.booktify.dto.response.RentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IRentService {
    RentResponse createRent(UUID userId, CreateRentRequest createRentRequest);

    RentResponse findRentById(Long id);

    Page<RentResponse> findRents(RentQueryFilter rentQueryFilter);

    RentResponse updateRent(Long id, UpdateRentRequest updateRentRequest);
}
