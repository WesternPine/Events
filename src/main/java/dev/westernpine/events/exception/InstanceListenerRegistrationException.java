package dev.westernpine.events.exception;

import java.lang.reflect.Method;
import java.util.Objects;

public class InstanceListenerRegistrationException extends RuntimeException {

    public InstanceListenerRegistrationException(Object instance, Method method) {
        super("Method %s was registered with an incorrect instance, which requires an instance of class %s. ".formatted(method.getName(), method.getDeclaringClass().getName())
        + (Objects.isNull(instance) ? "(Instance was null.)" : instance.getClass().getName() + " Was provided instead."));
    }
}
