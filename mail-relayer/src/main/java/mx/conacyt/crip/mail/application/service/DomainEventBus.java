package mx.conacyt.crip.mail.application.service;

import com.google.common.eventbus.EventBus;

/**
 * Clase estática que contiene el bus de eventos.
 */
public final class DomainEventBus {

    /**
     * El bus de eventos.
     */
    public static final EventBus EVENT_BUS = new EventBus();

    private DomainEventBus() {
    }

}
