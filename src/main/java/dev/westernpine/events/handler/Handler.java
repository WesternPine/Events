package dev.westernpine.events.handler;

import dev.westernpine.events.event.IEvent;

import java.lang.reflect.Method;

public record Handler(Object instance, Method method, Priority priority, boolean ignoreCanceled, Class<? extends IEvent> event){}