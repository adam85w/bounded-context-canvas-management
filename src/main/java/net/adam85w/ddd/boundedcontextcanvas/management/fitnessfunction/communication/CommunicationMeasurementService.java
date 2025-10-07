package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.communication;

import jakarta.transaction.Transactional;
import net.adam85w.ddd.boundedcontextcanvas.management.CanvasOperation;
import net.adam85w.ddd.boundedcontextcanvas.management.CanvasOperationNotifiable;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.MessageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Transactional(Transactional.TxType.REQUIRED)
@ConditionalOnProperty(prefix = "application.fitness-function", value = "enabled", havingValue = "true")
class CommunicationMeasurementService implements CanvasOperationNotifiable {

    private final CommunicationCounter counter;

    private final CommunicationMeasurementRepository repository;

    private final int pageSize;

    CommunicationMeasurementService(CommunicationCounter counter, CommunicationMeasurementRepository repository,
                                    @Value("${application.fitness-function.measurement.pagination.page-size}") int pageSize) {
        this.counter = counter;
        this.repository = repository;
        this.pageSize = pageSize;
    }

    Page<CommunicationMeasurement> retrieve(int pageNo) {
        return repository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @Override
    public void notify(CanvasOperation operation) {
        try {
            var summaries = counter.count(operation.getCreatedAt());
            repository.save(new CommunicationMeasurement(operation.getCanvasName(), operation.getOperationType(),
                    summaries.get(MessageType.QUERY.getName().toLowerCase()),
                    summaries.get(MessageType.COMMAND.getName().toLowerCase()),
                    summaries.get(MessageType.EVENT.getName().toLowerCase()),
                    LocalDateTime.now()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
