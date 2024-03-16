package dev.westernpine.events.handler;

import java.lang.reflect.Method;

public record HandlerReference(Object instance, Method method){}