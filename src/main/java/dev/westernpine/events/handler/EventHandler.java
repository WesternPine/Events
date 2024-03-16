package dev.westernpine.events.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {

    public EventPriority eventPriority = EventPriority.NORMAL;

    public static enum EventPriority {
        FIRST(6),
        HIGHEST(5),
        HIGH(4),
        NORMAL(3),
        LOW(2),
        LOWEST(1),
        LAST(0);

        public final int value;

        EventPriority(int value) {
            this.value = value;
        }

        public static EventPriority from(int value) {

            if(value >= FIRST.value)
                return FIRST;

            if(value <= LAST.value)
                return LAST;

            for(EventPriority priority : EventPriority.values()) {
                if(value == priority.value)
                    return priority;
            }

            return NORMAL;
        }

    }
}
