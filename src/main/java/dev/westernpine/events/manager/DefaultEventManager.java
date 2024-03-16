package dev.westernpine.events.manager;

import dev.westernpine.events.exception.*;
import dev.westernpine.events.handler.EventHandler;
import dev.westernpine.events.handler.Handler;
import dev.westernpine.events.event.IEvent;
import dev.westernpine.events.helper.EventHelper;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class DefaultEventManager implements IEventManager {

    private final HashMap<Class<? extends IEvent>, LinkedList<Handler>> listeners = new HashMap<>();

    public DefaultEventManager() { }

    /**
     * Register a listener for an event.
     * <br>If the listener is a static function, use null for the instance.
     * <br>Otherwise, all listeners must have an instance attached.
     * <br>
     * <br>Events are organized by 2 forms of priority.
     * <br>- 1. {@link EventHandler.EventPriority} Handlers the first set of calls and is the primary driving factor for ordering event executions.
     * <br>- 2. The order in which events are registered.
     * <br>
     * <br>Listeners and events can utilize a hierarchy of interfaces and extensions to organize and execute listeners and events.
     *
     * @param instance The instance of the class to invoke the listener with. Otherwise, null for static functions.
     * @param method   The method of the class or class instance to invoke when the event is called.
     * @throws EventHandlerListenerRegistrationException When a method is not annotated with an {@link EventHandler}.
     * @throws StaticListenerRegistrationException   When a method is static and an instance is supplied.
     * @throws InstanceListenerRegistrationException When a method is non-static and no/incorrect instance is supplied.
     * @throws AccessibleListenerRegistrationException When a method is private.
     * @throws ParameterListenerRegistrationException When a method does not contain only 1 parameter for an {@link IEvent} type.
     */
    @Override
    public Handler registerListener(Object instance, Method method) {

        // Some checks before registering.
        Optional<EventHandler> oHandler = EventHelper.getEventHandler(method);
        if(oHandler.isEmpty())
            throw new EventHandlerListenerRegistrationException(method);

        if(!EventHelper.isPublic(method))
            throw new AccessibleListenerRegistrationException(method);

        if(EventHelper.isStatic(method)) {
            if(!Objects.isNull(instance))
                throw new StaticListenerRegistrationException(instance, method);
        } else {
            if(Objects.isNull(instance) || method.getClass() != instance.getClass())
                throw new InstanceListenerRegistrationException(instance, method);
        }

        if(!EventHelper.isListeningForEvent(method))
            throw new ParameterListenerRegistrationException(method);

        Class<? extends IEvent> clazz = (Class<? extends IEvent>) method.getParameters()[0].getType();

        EventHandler.EventPriority priority = oHandler.get().eventPriority;
        priority = Objects.isNull(priority) ? EventHandler.EventPriority.NORMAL : priority; // Null safety check.

        Handler handler = new Handler(instance, method, priority, clazz);

        if(!this.listeners.containsKey(clazz))
            listeners.put(clazz, new LinkedList<>());

        // If we add the listener in the proper order,
        // then we won't need to sort when we call the listener.

        LinkedList<Handler> handlers = listeners.get(clazz);
        if(handlers.isEmpty()) {
            handlers.add(handler);
        } else if (handler.priority() == EventHandler.EventPriority.LAST) { // If we already know it's the last priority, we know we want to add it last.
            handlers.addFirst(handler);
        } else {
            int index = -1;
            for(Handler h : handlers) {
                index++;
                // HIGHEST(5) < NORMAL(3) = false (Don't Add)
                // NORMAL(3) < NORMAL(3) = false (Don't Add)
                // LAST(0) < NORMAL(3) = true (Add)
                if(h.priority().value < handler.priority().value) { // We don't check = here to make sure we're adding the handler last when the event priority is the same.
                    handlers.add(index, handler);
                } else if (index == handlers.size()-1) { // Add as last handler if we've reached the end.
                    handlers.add(handler);
                }
            }
        }

        return handler;
    }

    /**
     * Unregisters the handler from the event listeners.
     * @param handler The handler object returned from registering the listener.
     */
    @Override
    public void unregisterListener(Handler handler) {
        LinkedList<Handler> handlers = listeners.get(handler.event());
        handlers.remove(handler);
        if(handlers.isEmpty()) {
            listeners.remove(handler.event());
        }
    }

    /**
     * Call all listeners listening to the event supplied.
     * <br>
     * <br>Listeners and events can utilize a hierarchy of interfaces and extensions to organize and execute listeners and events.
     *
     * @param event Finds all functions listening to the specified event type.
     * @return A map of all the handlers that had exceptions in their executions.
     */
    @Override
    public Map<Handler, Exception> call(IEvent event) {
        Map<Handler, Exception> exceptions = new HashMap<>();
        for(Handler handler : getHandlers(event.getClass())) {
            try {
                handler.method().invoke(handler.instance(), event);
            } catch (Exception exception) {
                exceptions.put(handler, exception);
            }
        }
        return exceptions;
    }

    /**
     * Get all listener events for this event manager.
     * @return An unmodifiable set of events this manager has handlers that are listening for.
     */
    public Set<Class<? extends IEvent>> getListeners() {
        return Collections.unmodifiableSet(listeners.keySet());
    }

    /**
     * The way we find listeners for an event will not find all assignable event parent events.
     * @param event The event to identify handlers for.
     * @return A list of handlers for the event.
     */
    public List<Handler> getHandlers(Class<? extends IEvent> event) {
        LinkedList<Handler> handlers = this.listeners.get(event);
        if(handlers == null) {
            handlers = new LinkedList<>();
        }
        return Collections.unmodifiableList(handlers);
    }
}
