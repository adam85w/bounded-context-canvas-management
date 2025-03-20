package net.adam85w.ddd.boundedcontextcanvas.management;

import jakarta.transaction.Transactional;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Set;

@Component
@Transactional
class CanvasOperationNotificationScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CanvasOperationNotificationScheduler.class);

    private static final Duration TEN_YEARS = Duration.ofDays(3650);

    private final LockProvider lockProvider;

    private final CanvasOperationRepository repository;

    private final Set<CanvasOperationNotifiable> notifiables;

    CanvasOperationNotificationScheduler(LockProvider lockProvider, CanvasOperationRepository repository, Set<CanvasOperationNotifiable> notifiables) {
        this.lockProvider = lockProvider;
        this.repository = repository;
        this.notifiables = notifiables;
    }

    @Scheduled(fixedDelayString = "${application.operation-notification.scheduler}")
    void run() {
        LOGGER.info("Run notification scheduler");
        Optional<SimpleLock> lock = lockProvider.lock(new LockConfiguration(LocalDateTime.now().toInstant(ZoneOffset.UTC), "notification", TEN_YEARS, TEN_YEARS));
        try {
            for (CanvasOperation operation : repository.findAllByProcessedIsFalseOrderByCreatedAtAsc()) {
                LOGGER.info("Processing operation {}", operation);
                notifiables.forEach(notifiable -> notifiable.notify(operation));
                LOGGER.info("Updating operation {}", operation);
                operation.update(true);
                repository.save(operation);
                LOGGER.info("Operation {} completed", operation);
            }
        } catch (Exception e) {
            LOGGER.error("Error while processing operations", e);
            throw e;
        } finally {
            lock.ifPresent(SimpleLock::unlock);
            LOGGER.info("Stop notification scheduler");
        }
    }
}
