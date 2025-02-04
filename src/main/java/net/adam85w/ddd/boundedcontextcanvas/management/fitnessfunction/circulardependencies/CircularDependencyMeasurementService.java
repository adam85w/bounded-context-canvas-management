package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.circulardependencies;

import jakarta.transaction.Transactional;
import net.adam85w.ddd.boundedcontextcanvas.management.CanvasOperationNotifiable;
import net.adam85w.ddd.boundedcontextcanvas.management.CanvasOperation;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Transactional(Transactional.TxType.REQUIRED)
class CircularDependencyMeasurementService implements CanvasOperationNotifiable {

    private final CircularDependencyDiscoverer discoverer;

    private final CircularDependencyMeasurementRepository repository;

    CircularDependencyMeasurementService(CircularDependencyDiscoverer discoverer, CircularDependencyMeasurementRepository repository) {
        this.discoverer = discoverer;
        this.repository = repository;
    }

    Iterable<CircularDependencyMeasurement> retrieve() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public void notify(CanvasOperation operation) {
        try {
            repository.save(new CircularDependencyMeasurement(
                    operation.getCanvasName(),
                    operation.getOperationType(),
                    discoverer.discover().size(),
                    LocalDateTime.now()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
