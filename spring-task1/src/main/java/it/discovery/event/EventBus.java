package it.discovery.event;

import it.discovery.log.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

import java.util.List;

@RequiredArgsConstructor
public class EventBus {

    private final List<Logger> loggers;

    @EventListener
    public void onLogEvent(LogEvent logEvent) {
        loggers.forEach(logger -> logger.log(logEvent.message()));
    }

}
