package id.my.cupcakez.booktify.domain.rent.service;

import id.my.cupcakez.booktify.constant.StatusRent;
import id.my.cupcakez.booktify.domain.book.repository.IBookRepository;
import id.my.cupcakez.booktify.domain.rent.repository.IRentRepository;
import id.my.cupcakez.booktify.domain.user.repository.IUserRepository;
import id.my.cupcakez.booktify.dto.request.CreateRentRequest;
import id.my.cupcakez.booktify.dto.request.UpdateRentRequest;
import id.my.cupcakez.booktify.dto.response.RentResponse;
import id.my.cupcakez.booktify.entity.BookEntity;
import id.my.cupcakez.booktify.entity.RentEntity;
import id.my.cupcakez.booktify.entity.UserEntity;
import id.my.cupcakez.booktify.exception.CustomException;
import id.my.cupcakez.booktify.util.mapper.IRentMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentService implements IRentService {
    private IRentRepository rentRepository;
    private IBookRepository bookRepository;
    private IUserRepository userRepository;
    private IRentMapper rentMapper;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    public RentService(IRentRepository rentRepository, IBookRepository bookRepository, IUserRepository userRepository,IRentMapper rentMapper){
        this.rentRepository = rentRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.rentMapper = rentMapper;
    }

    @Override
    @Transactional
    public RentResponse createRent(UUID userId, CreateRentRequest createRentRequest) {
        Optional<BookEntity> bookEntity = bookRepository.getBookByIdForUpdate(createRentRequest.getBookId());

        return bookEntity.map(b -> {
            if (b.getStock() < createRentRequest.getQuantity()) {
                throw new CustomException("Book stock is not enough", HttpStatus.BAD_REQUEST);
            }

            b.setStock(b.getStock() - createRentRequest.getQuantity());
            bookRepository.save(b);

            LocalDate rentedUntil = LocalDate.parse(createRentRequest.getRentedUntil(), dateFormatter);

            if (rentedUntil.isBefore(LocalDate.now())) {
                throw new CustomException("Rented until must be greater than today", HttpStatus.BAD_REQUEST);
            }

            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

            RentEntity rentData = RentEntity.builder()
                    .book(b)
                    .user(user)
                    .quantity(createRentRequest.getQuantity())
                    .status(StatusRent.PENDING)
                    .rentedUntil(rentedUntil)
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
            if (updateRentRequest.getStatus() == rent.getStatus()){
                throw new CustomException("Status is same", HttpStatus.BAD_REQUEST);
            }

            if(updateRentRequest.getStatus() == StatusRent.RETURNED || updateRentRequest.getStatus() == StatusRent.REJECTED){
                Optional<BookEntity> bookEntity = bookRepository.getBookByIdForUpdate(rent.getBook().getId());
                bookEntity.map(book -> {
                    book.setStock(book.getStock() + rent.getQuantity());
                    bookRepository.save(book);
                    return book;
                }).orElseThrow(
                        () -> new CustomException("Book not found", HttpStatus.NOT_FOUND)
                );
            }

            rent.setStatus(updateRentRequest.getStatus());

            return rentMapper.toRentResponse(rentRepository.save(rent));
        }).orElseThrow(
                () -> new CustomException("Rent not found", HttpStatus.NOT_FOUND)
        );
    }


}
