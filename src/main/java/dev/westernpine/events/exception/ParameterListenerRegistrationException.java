package dev.westernpine.events.exception;

import java.lang.reflect.Method;

public class ParameterListenerRegistrationException extends RuntimeException {

    public ParameterListenerRegistrationException(Method method) {
        super("Method %s does not have only 1 parameter of type IEvent.".formatted(method.getName()));
    }
}
