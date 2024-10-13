package com.oneune.mater.rest.main.events;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@ToString
public class CarUpdatedEvent extends ApplicationEvent {

    Long carId;

    public CarUpdatedEvent(Object source, Long carId) {
        super(source);
        this.carId = carId;
    }
}
