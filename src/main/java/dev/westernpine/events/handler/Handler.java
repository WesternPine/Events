package dev.westernpine.events.handler;

import dev.westernpine.events.event.IEvent;

import java.lang.reflect.Method;

public record Handler(Object instance, Method method, EventHandler.EventPriority priority, Class<? extends IEvent> event){}