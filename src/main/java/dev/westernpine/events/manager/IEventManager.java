package dev.westernpine.events.manager;

import dev.westernpine.events.exception.AccessibleListenerRegistrationException;
import dev.westernpine.events.exception.InstanceListenerRegistrationException;
import dev.westernpine.events.exception.ParameterListenerRegistrationException;
import dev.westernpine.events.exception.StaticListenerRegistrationException;
import dev.westernpine.events.handler.Handler;
import dev.westernpine.events.event.IEvent;

import java.lang.reflect.Method;
import java.util.Map;

public interface IEventManager {

    /**
     * Register a listener for an event.
     * <br>If the listener is a static function, use null for the instance.
     * <br>Otherwise, all listeners must have an instance attached.
     * <br>
     * <br>Events are organized by 2 forms of priority.
     * <br>- 1. {@code EventHandler.Priority} Handlers the first set of calls and is the primary driving factor for ordering event executions.
     * <br>- 2. The order in which events are registered.
     * <br>
     * <br>Listeners and events can utilize a hierarchy of interfaces and extensions to organize and execute listeners and events.
     *
     * @param instance The instance of the class to invoke the listener with. Otherwise, null for static functions.
     * @param method   The method of the class or class instance to invoke when the event is called.
     * @throws StaticListenerRegistrationException   When a method is static and an instance is supplied.
     * @throws InstanceListenerRegistrationException When a method is non-static and no/incorrect instance is supplied.
     * @throws AccessibleListenerRegistrationException When a method is private.
     * @throws ParameterListenerRegistrationException When a method does not contain only 1 parameter for an IEvent.
     *
     * @return Handler An object containing required objects for ordering, calling, adding, and removing events.
     */
    public Handler registerListener(Object instance, Method method);

    /**
     * Unregisters the handler from the event listeners.
     * @param handler The handler object returned from registering the listener.
     */
    public void unregisterListener(Handler handler);

    /**
     * Call all listeners listening to the event supplied.
     * <br>
     * <br>Listeners and events can utilize a hierarchy of interfaces and extensions to organize and execute listeners and events.
     *
     * @param event Finds all functions listening to the specified event type.
     * @return A map of all the handlers that had exceptions in their executions.
     */
    public Map<Handler, Exception> call(IEvent event);

}
