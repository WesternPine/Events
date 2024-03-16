package dev.westernpine.events.event;

/**
 * Interface exists as a form of middle-man between Generic implementation, and using "Object". Without this, all events would need to be of the same type for generics, or extra processing time for understanding the type of "Object" used for "Object" implementation.
 * <br>
 * <br>All implementations of this class should contain all the contextual information to complete execution.
 */
public interface IEvent {
}
