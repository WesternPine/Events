package dev.westernpine.events.helper;

import dev.westernpine.events.event.IEvent;
import dev.westernpine.events.exception.ParameterListenerRegistrationException;
import dev.westernpine.events.handler.EventHandler;
import dev.westernpine.events.handler.HandlerReference;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class EventHelper {

    /**
     * Get the {@link EventHandler} Annotation for and event listener.
     * @param method The method to get the {@link EventHandler} for.
     * @return An optional of the {@link EventHandler} annotation.
     */
    public static Optional<EventHandler> getEventHandler(Method method) {
        return Optional.ofNullable(method.getAnnotation(EventHandler.class));
    }

    public static boolean isPublic(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    public static boolean isStatic(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    /**
     * Returns whether the method is listening for an event.
     * @param method The potential event listener.
     * @return If listening for a {@link IEvent} type.
     */
    public static boolean isListeningForEvent(Method method) {
        int params = method.getParameterCount();
        if(params != 1)
            return false;

        Class<?> param = method.getParameters()[0].getType();
        boolean isEvent =  IEvent.class.isAssignableFrom(param);
        return isEvent;
    }

    /**
     * This method handles both class instances and class references.
     * <br>
     * <br><u><b>Both clazz and instance cannot be null!</b></u>
     * <br>
     * <br>Providing null instance will only look for static methods.
     * <br>Providing an instance will automatically override the clazz provided.
     * <br>
     * <br>This function does all the pre-validation checks for identifying proper event listeners.
     *
     * @param clazz A class type to find static methods in.
     * @param instance An instance to find methods in.
     * @return A list of valid HandlerReferences to be used as event listeners.
     */
    public static LinkedList<HandlerReference> getHandlerReferences(Class<?> clazz, Object instance) {
        if(!Objects.isNull(instance))
            clazz = instance.getClass();
        else if (Objects.isNull(clazz))
            throw new NullPointerException("Both clazz and instance cannot be null!");

        LinkedList<HandlerReference> references = new LinkedList<>();
        for(Method method : clazz.getMethods()) {
            Optional<EventHandler> oHandler = getEventHandler(method);

            if(oHandler.isEmpty())
                continue;

            if(!isPublic(method))
                continue;

            if(!isListeningForEvent(method))
                continue;

            if(Objects.isNull(instance) && !isStatic(method))
                continue;

            references.add(new HandlerReference(isStatic(method) ? null : instance, method));
        }

        return references;
    }

}
