# Events

Events is an event framework inspired by languages like C#. It aims to implement code cleanliness and implementation anonymity.

## Examples & Purpose

When you need to run code from different jars or unscoped projects, things can get messy. This also:
- Is very intensive.
- Has no event execution priority.
- Only allows for registering ALL possible handlers, instead of letting you decide.
```Java
        // Map of class type to instance.
        Map<Class<?>, Object> classInstances = new HashMap<>();
        
        // Register ALL handlers from any class.
        classInstances.put(this.getClass(), this);

        // Calling; This is extremely intensive.
        for(Class<?> clazz : classInstances.keySet()) {
            Method[] methods = clazz.getMethods();
            for(Method method : methods) {
                if(!Modifier.isPublic(method.getModifiers()))
                    continue;
                if(SomeEvent.class.isAssignableFrom(method.getParameters()[0].getType())) {
                    method.invoke(Modifier.isStatic(method.getModifiers()) ? null : classInstances.get(clazz), new SomeEvent());
                }
            }
        }
```

With events, the process is simple.
```Java
        // Register handlers.
        EventHelper.getHandlerReferences(null, new ListenerClass()).forEach(reference ->
                this.eventManager.registerListener(reference.instance(), reference.method())
        );

        // And finally call any event you need from anywhere.
        this.eventManager.call(new SomeEvent());
```

## Quick Guide

Firstly, you'll need an EventManager.
```Java
IEventManager eventManager = new DefaultEventManager();
```

Then you can start by making any events you need.
```Java
public class Event implements IEvent {
  public string msg;
  public Event(string msg) {
    this.msg = msg;
  }
}
```

Now create your listeners.
```Java
public class Listeners {
  @EventHandler(priority = Priority.LOW)
  public void listener2(Event event) {
    System.out.println("This was called second!" + event.msg);
  }

  @EventHandler
  public void listener1(Event event) {
    System.out.println("This was callsed first!" + event.msg);
  }
}
```

Then you can (un)register events at any time.
```Java
// Register all handlers in a class, and store the handler object reference.
List<Handler> handlers = EventHelper.getHandlerReferences(null, new ListenerClass()).map(reference -> 
  this.eventManager.registerListener(reference.instance(), reference.method())
).toList();

// If you want to unregister all handlers.
handlers.forEach(eventManager::unregisterListener);
```

And finally, call your events!
```Java
eventManager.call(new Event("")); // Messages should appear in order.
```