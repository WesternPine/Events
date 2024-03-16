package dev.westernpine.events.manager;

import dev.westernpine.events.event.IEvent;
import dev.westernpine.events.handler.EventHandler;
import dev.westernpine.events.handler.Handler;
import dev.westernpine.events.handler.HandlerReference;
import dev.westernpine.events.handler.Priority;
import dev.westernpine.events.helper.EventHelper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DefaultEventManagerTest {

    private DefaultEventManager eventManager;
    private ListenerTest listenerTest;

    public DefaultEventManagerTest() {
        this.eventManager = new DefaultEventManager();
        this.listenerTest = new ListenerTest();
    }

    @org.junit.jupiter.api.Test
    void registerListener() {
        List<HandlerReference> references = EventHelper.getHandlerReferences(null, listenerTest);
        references.forEach(reference -> this.eventManager.registerListener(reference.instance(), reference.method()));
        Assertions.assertFalse(eventManager.getListeners().isEmpty());
    }

    @org.junit.jupiter.api.Test
    void unregisterListener() {
        List<HandlerReference> references = EventHelper.getHandlerReferences(null, listenerTest);
        List<Handler> handlers = references.stream().map(reference -> this.eventManager.registerListener(reference.instance(), reference.method())).toList();

        this.eventManager.unregisterListener(handlers.get(0));
        Assertions.assertEquals(eventManager.getListeners().size(), 1);
    }

    @org.junit.jupiter.api.Test
    void call() {
        List<HandlerReference> references = EventHelper.getHandlerReferences(null, listenerTest);
        references.forEach(reference -> this.eventManager.registerListener(reference.instance(), reference.method()));
        Assertions.assertTrue(this.eventManager.call(new EventTest1(1)).isEmpty());
        Assertions.assertTrue(this.eventManager.call(new EventTest1(2)).isEmpty());// We're doing 2 calls (addition) to make sure we're not calling other events (subtraction) each time as well.
        Assertions.assertTrue(this.eventManager.call(new EventTest2(1)).isEmpty());
        Assertions.assertTrue(this.eventManager.call(new EventTest2(0)).isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getListeners() {
        List<HandlerReference> references = EventHelper.getHandlerReferences(null, listenerTest);
        references.forEach(reference -> this.eventManager.registerListener(reference.instance(), reference.method()));
        Assertions.assertEquals(eventManager.getListeners().size(), 2);
    }

    @org.junit.jupiter.api.Test
    void getHandlers() {
        List<HandlerReference> references = EventHelper.getHandlerReferences(null, listenerTest);
        references.forEach(reference -> this.eventManager.registerListener(reference.instance(), reference.method()));
        eventManager.getListeners().forEach(event -> Assertions.assertFalse(this.eventManager.getHandlers(event).isEmpty()));
    }

}

class ListenerTest {

    private int i = 0;

    @EventHandler
    public void TestEventListener1(EventTest1 event) {
        Assertions.assertEquals(i, event.value);
    }

    @EventHandler(priority = Priority.FIRST)
    public void PriorityTest1(EventTest1 event) {
        i++;
    }

    @EventHandler
    public void TestEventListener2(EventTest2 event) {
        Assertions.assertEquals(i, event.value);
    }

    @EventHandler(priority = Priority.FIRST)
    public void PriorityTest2(EventTest2 event) {
        i--;
    }


}

class EventTest1 implements IEvent {
    public int value;

    public EventTest1(int value) {
        this.value = value;
    }
}
class EventTest2 implements IEvent {
    public int value;

    public EventTest2(int value) {
        this.value = value;
    }
}