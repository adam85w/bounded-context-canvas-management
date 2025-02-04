package net.adam85w.ddd.boundedcontextcanvas.management;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Transactional
class CanvasOperationNotificationScheduler {

    private final CanvasOperationRepository repository;

    private final Set<CanvasOperationNotifiable> notifiables;

    CanvasOperationNotificationScheduler(CanvasOperationRepository repository, Set<CanvasOperationNotifiable> notifiables) {
        this.repository = repository;
        this.notifiables = notifiables;
    }

    @Scheduled(fixedDelayString = "${application.operation-notification.scheduler}")
    void run() {
        for (CanvasOperation operation : repository.findAllByProcessedIsFalse()) {
            notifiables.forEach(notifiable -> notifiable.notify(operation));
            operation.update(true);
            repository.save(operation);
        }
    }
}
