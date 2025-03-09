package net.adam85w.ddd.boundedcontextcanvas.management;

import jakarta.transaction.Transactional;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Transactional
class CanvasOperationNotificationScheduler {

    private final static Logger LOGGER = LoggerFactory.getLogger(CanvasOperationNotificationScheduler.class);

    private final CanvasOperationRepository repository;

    private final Set<CanvasOperationNotifiable> notifiables;

    CanvasOperationNotificationScheduler(CanvasOperationRepository repository, Set<CanvasOperationNotifiable> notifiables) {
        this.repository = repository;
        this.notifiables = notifiables;
    }

    @Scheduled(fixedDelayString = "${application.operation-notification.scheduler}")
    @SchedulerLock(name = "notification", lockAtLeastFor = "${application.operation-notification.lock}", lockAtMostFor = "${application.operation-notification.lock}")
    void run() {
        LOGGER.info("Run notification scheduler");
        for (CanvasOperation operation : repository.findAllByProcessedIsFalse()) {
            LOGGER.info("Processing operation {}", operation);
            notifiables.forEach(notifiable -> notifiable.notify(operation));
            LOGGER.info("Updating operation {}", operation);
            operation.update(true);
            repository.save(operation);
            LOGGER.info("Operation {} completed", operation);
        }
        LOGGER.info("Stop notification scheduler");
    }
}
