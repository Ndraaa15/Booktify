package id.my.cupcakez.booktify.pubsub.listener;

import id.my.cupcakez.booktify.pubsub.event.OverdueRentEvent;
import id.my.cupcakez.booktify.util.scheduler.Scheduler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OverdueRentListener implements ApplicationListener<OverdueRentEvent> {
    private static final Logger logger = LogManager.getLogger(OverdueRentListener.class);
    @Override
    public void onApplicationEvent(OverdueRentEvent event) {
        // Todo : implement email notification
        logger.info("overdue rent listener triggered");
    }
}
