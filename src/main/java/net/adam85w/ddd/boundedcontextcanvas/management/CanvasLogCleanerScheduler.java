package net.adam85w.ddd.boundedcontextcanvas.management;


import jakarta.transaction.Transactional;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Transactional
@ConditionalOnProperty(prefix = "application.fitness-function", value = "enabled", havingValue = "true")
class CanvasLogCleanerScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CanvasLogCleanerScheduler.class);

    private final CanvasLogRepository canvasLogRepository;

    private final CanvasOperationRepository canvasOperationRepository;

    CanvasLogCleanerScheduler(CanvasLogRepository canvasLogRepository, CanvasOperationRepository canvasOperationRepository) {
        this.canvasLogRepository = canvasLogRepository;
        this.canvasOperationRepository = canvasOperationRepository;
    }

    @Scheduled(fixedDelayString = "${application.canvas-log-cleaner.scheduler}")
    @SchedulerLock(name = "log-clean", lockAtMostFor = "1m", lockAtLeastFor = "1m")
    public void clean() {
        LOGGER.info("Run canvas log cleaner scheduler");
        try {
            var processedOperations = canvasOperationRepository.findAllByProcessedIsTrue();
            var ids = processedOperations.stream().map(CanvasOperation::getId).collect(Collectors.toSet());
            canvasLogRepository.deleteAllByCanvasOperationIdIn(ids);
        } catch (Exception e) {
            LOGGER.error("Error while cleaning log", e);
            throw e;
        } finally {
            LOGGER.info("Stop canvas log cleaner scheduler");
        }

    }
}
