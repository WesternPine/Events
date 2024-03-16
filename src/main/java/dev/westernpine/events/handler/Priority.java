package dev.westernpine.events.handler;

public enum Priority {
    FIRST(6),
    HIGHEST(5),
    HIGH(4),
    NORMAL(3),
    LOW(2),
    LOWEST(1),
    LAST(0);

    public final int value;

    Priority(int value) {
        this.value = value;
    }

    public static Priority from(int value) {

        if(value >= FIRST.value)
            return FIRST;

        if(value <= LAST.value)
            return LAST;

        for(Priority priority : Priority.values()) {
            if(value == priority.value)
                return priority;
        }

        return NORMAL;
    }

}