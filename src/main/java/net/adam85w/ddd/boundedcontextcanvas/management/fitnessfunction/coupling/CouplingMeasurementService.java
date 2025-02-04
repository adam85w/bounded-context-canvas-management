package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.coupling;

import jakarta.transaction.Transactional;
import net.adam85w.ddd.boundedcontextcanvas.management.CanvasOperation;
import net.adam85w.ddd.boundedcontextcanvas.management.CanvasOperationNotifiable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Transactional(Transactional.TxType.REQUIRED)
class CouplingMeasurementService implements CanvasOperationNotifiable {

    private final CouplingCounter counter;

    private final CouplingMeasurementRepository repository;

    CouplingMeasurementService(CouplingCounter counter, CouplingMeasurementRepository repository) {
        this.counter = counter;
        this.repository = repository;
    }

    Iterable<CouplingMeasurement> retrieve() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public void notify(CanvasOperation operation) {
        try {
            var summary = counter.count();
            repository.save(new CouplingMeasurement(
                    operation.getCanvasName(),
                    operation.getOperationType(),
                    summary.componentsSum(),
                    summary.afferentCouplingsSum(),
                    summary.efferentCouplingsSum(),
                    LocalDateTime.now()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
