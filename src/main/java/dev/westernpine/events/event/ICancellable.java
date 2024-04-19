package dev.westernpine.events.event;

/**
 * ICancellable is intended to be utilized alongside {@code IEvent},
 * to skip handler executions if their ignoreCanceled isn't true.
 */
public interface ICancellable {
    /**
     * Whether the event was canceled.
     * <br>If this is true, no other handler will be called if ignoreCancelled isn't true.
     *
     * @return True if the event was canceled, false otherwise.
     */
    public boolean isCanceled();

    /**
     * Set the event as canceled.
     * <br>This will skip handler execution if their ignoreCancelled isn't true.
     *
     * @param canceled True if canceled, false otherwise.
     */
    public void setCanceled(boolean canceled);
}
