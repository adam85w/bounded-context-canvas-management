package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.circulardependencies;

import jakarta.transaction.Transactional;
import net.adam85w.ddd.boundedcontextcanvas.management.CanvasOperation;
import net.adam85w.ddd.boundedcontextcanvas.management.CanvasOperationNotifiable;
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
class CircularDependencyMeasurementService implements CanvasOperationNotifiable {

    private final CircularDependencyDiscoverer discoverer;

    private final CircularDependencyMeasurementRepository repository;

    private final int pageSize;

    CircularDependencyMeasurementService(CircularDependencyDiscoverer discoverer, CircularDependencyMeasurementRepository repository,
                                         @Value("${application.fitness-function.measurement.pagination.page-size}") int pageSize) {
        this.discoverer = discoverer;
        this.repository = repository;
        this.pageSize = pageSize;
    }

    Page<CircularDependencyMeasurement> retrieve(int pageNo) {
        return repository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @Override
    public void notify(CanvasOperation operation) {
        try {
            repository.save(new CircularDependencyMeasurement(
                    operation.getCanvasName(),
                    operation.getOperationType(),
                    discoverer.discover(operation).size(),
                    LocalDateTime.now()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
