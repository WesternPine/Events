package dev.westernpine.events.exception;

import java.lang.reflect.Method;

public class StaticListenerRegistrationException extends RuntimeException {

    public StaticListenerRegistrationException(Object instance, Method method) {
        super("Method %s was registered with instance %s, but the method is static and does not require an instance. (Please use null for the instance)".formatted(method.getName(), instance.getClass().getName()));
    }
}
