package dev.westernpine.events.exception;

import java.lang.reflect.Method;

public class EventHandlerListenerRegistrationException extends RuntimeException {

    public EventHandlerListenerRegistrationException(Method method) {
        super("Method %s is not an EventHandler.".formatted(method.getName()));
    }
}
