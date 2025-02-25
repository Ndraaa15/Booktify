package id.my.cupcakez.booktify.pubsub.listener;

import id.my.cupcakez.booktify.pubsub.event.OverdueRentEvent;
import id.my.cupcakez.booktify.util.mail.Mail;
import id.my.cupcakez.booktify.util.scheduler.Scheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class OverdueRentListener implements ApplicationListener<OverdueRentEvent> {
    private Mail mail;
    private static final Logger logger = LogManager.getLogger(OverdueRentListener.class);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    public OverdueRentListener(Mail mail) {
        this.mail = mail;
    }

    @Override
    public void onApplicationEvent(OverdueRentEvent event) {
        event.getOverdueRents().forEach(rent -> {
            mail.sendSimpleMessage(
                    rent.getUser().getEmail(),
                    "Overdue Rent",
                    "Dear " + rent.getUser().getName() + ",\n\n" +
                            "You have an overdue rent with the following details:\n" +
                            "Book Title: " + rent.getBook().getTitle() + "\n" +
                            "Due Date: " + rent.getRentedUntil().format(dateFormatter) + "\n\n" +
                            "Please return the book as soon as possible.\n\n" +
                            "Thank you."
            );
        });

        logger.info("overdue rent listener triggered");
    }
}
