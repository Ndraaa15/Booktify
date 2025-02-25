package id.my.cupcakez.booktify.pubsub.event;

import id.my.cupcakez.booktify.entity.RentEntity;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.util.List;


public class OverdueRentEvent extends ApplicationEvent {
    private List<RentEntity> overdueRents;

    public OverdueRentEvent(Object source, List<RentEntity> overdueRents) {
        super(source);
        this.overdueRents = overdueRents;
    }

    public List<RentEntity> getOverdueRents() {
        return overdueRents;
    }
}
