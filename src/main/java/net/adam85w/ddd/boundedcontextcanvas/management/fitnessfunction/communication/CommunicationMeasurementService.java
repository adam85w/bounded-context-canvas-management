package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.communication;

import jakarta.transaction.Transactional;
import net.adam85w.ddd.boundedcontextcanvas.management.CanvasOperation;
import net.adam85w.ddd.boundedcontextcanvas.management.CanvasOperationNotifiable;
import net.adam85w.ddd.boundedcontextcanvas.model.communication.MessageType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Transactional(Transactional.TxType.REQUIRED)
class CommunicationMeasurementService implements CanvasOperationNotifiable {

    private final CommunicationCounter counter;

    private final CommunicationMeasurementRepository repository;

    CommunicationMeasurementService(CommunicationCounter counter, CommunicationMeasurementRepository repository) {
        this.counter = counter;
        this.repository = repository;
    }

    Iterable<CommunicationMeasurement> retrieve() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public void notify(CanvasOperation operation) {
        try {
            var summaries = counter.count();
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
