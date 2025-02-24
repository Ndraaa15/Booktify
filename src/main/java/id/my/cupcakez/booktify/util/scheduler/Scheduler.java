package id.my.cupcakez.booktify.util.scheduler;

import id.my.cupcakez.booktify.constant.StatusRent;
import id.my.cupcakez.booktify.domain.rent.repository.IRentRepository;
import id.my.cupcakez.booktify.entity.RentEntity;
import id.my.cupcakez.booktify.pubsub.event.OverdueRentEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class Scheduler {
    private IRentRepository rentRepository;
    private static final Logger logger = LogManager.getLogger(Scheduler.class);
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public Scheduler(IRentRepository rentRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.rentRepository = rentRepository;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Jakarta")
    public void checkRentOverdue() {
        logger.info("checking overdue rent...");

        Integer totalAffectedRents = rentRepository.updateOverdueRent(LocalDate.now(), StatusRent.ON_RENT, StatusRent.OVERDUE);

        if (totalAffectedRents == 0) {
            logger.info("no overdue rent found");
        }else {
            List<RentEntity> overdueRents = rentRepository.findAll().stream()
                    .filter(rent -> rent.getStatus().equals(StatusRent.OVERDUE))
                    .toList();

            applicationEventPublisher.publishEvent(new OverdueRentEvent(this, overdueRents));

            logger.info("overdue rent successfully updated, got {} rents", totalAffectedRents);
        }
    }
}
