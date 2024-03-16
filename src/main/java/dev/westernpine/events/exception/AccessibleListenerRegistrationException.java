package dev.westernpine.events.exception;

import java.lang.reflect.Method;

public class AccessibleListenerRegistrationException extends RuntimeException {

    public AccessibleListenerRegistrationException(Method method) {
        super("Method %s is not public.".formatted(method.getName()));
    }
}
