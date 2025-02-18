package id.my.cupcakez.booktify.domain.rent.service;

import id.my.cupcakez.booktify.constant.StatusRent;
import id.my.cupcakez.booktify.domain.book.repository.IBookRepository;
import id.my.cupcakez.booktify.domain.rent.repository.IRentRepository;
import id.my.cupcakez.booktify.dto.request.CreateRentRequest;
import id.my.cupcakez.booktify.dto.request.UpdateRentRequest;
import id.my.cupcakez.booktify.dto.response.RentResponse;
import id.my.cupcakez.booktify.entity.BookEntity;
import id.my.cupcakez.booktify.entity.RentEntity;
import id.my.cupcakez.booktify.exception.CustomException;
import id.my.cupcakez.booktify.util.mapper.IRentMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.quartz.LocalDataSourceJobStore;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentService implements IRentService {
    private IRentRepository rentRepository;
    private IBookRepository bookRepository;
    private IRentMapper rentMapper;

    @Autowired
    public RentService(IRentRepository rentRepository, IBookRepository bookRepository, IRentMapper rentMapper){
        this.rentRepository = rentRepository;
        this.bookRepository = bookRepository;
        this.rentMapper = rentMapper;
    }

    @Override
    @Transactional
    public RentResponse createRent(UUID userId, CreateRentRequest createRentRequest) {
        Optional<BookEntity> bookEntity = bookRepository.getBookByIdForUpdate(createRentRequest.getBookId());
        return bookEntity.map(b -> {
            if(b.getStock() < createRentRequest.getQuantity()){
                throw new CustomException("Book stock is not enough", HttpStatus.BAD_REQUEST);
            }
            b.setStock(b.getStock() - createRentRequest.getQuantity());
            bookRepository.save(b);

            LocalDate rentedUntil = LocalDate.parse(createRentRequest.getRentedUntil());

            RentEntity rentData = RentEntity.builder()
                    .bookId(createRentRequest.getBookId())
                    .quantity(createRentRequest.getQuantity())
                    .rentedUntil(rentedUntil)
                    .userId(userId)
                    .build();

            RentEntity rentEntity = rentRepository.save(rentData);

            return rentMapper.toRentResponse(rentEntity);
        }).orElseThrow(
                () -> new CustomException("Book not found", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public RentResponse getRentById(Long id) {
        Optional<RentEntity> rentEntity = rentRepository.findById(id);
        return rentEntity.map(rentMapper::toRentResponse).orElseThrow(
                () -> new CustomException("Rent not found", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public Page<RentResponse> getRents(Pageable pageable) {
        return rentRepository.findAll(pageable).map(rentMapper::toRentResponse);
    }

    @Override
    @Transactional
    public RentResponse updateRent(Long id, UpdateRentRequest updateRentRequest) {
        Optional<RentEntity> rentEntity = rentRepository.findById(id);
        return rentEntity.map(rent -> {
            Optional<BookEntity> bookEntity = bookRepository.getBookByIdForUpdate(rent.getBookId());

            if(bookEntity.isEmpty()){
                throw new CustomException("Book not found", HttpStatus.NOT_FOUND);
            }

            BookEntity book = bookEntity.get();
            book.setStock(book.getStock() + rent.getQuantity());

            rent.setStatus(StatusRent.valueOf(updateRentRequest.getStatus()));

            bookRepository.save(book);

            return rentMapper.toRentResponse(rentRepository.save(rent));
        }).orElseThrow(
                () -> new CustomException("Rent not found", HttpStatus.NOT_FOUND)
        );
    }


}
