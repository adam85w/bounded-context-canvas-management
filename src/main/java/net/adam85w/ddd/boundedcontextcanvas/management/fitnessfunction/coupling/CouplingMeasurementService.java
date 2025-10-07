package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.coupling;

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
class CouplingMeasurementService implements CanvasOperationNotifiable {

    private final CouplingCounter counter;

    private final CouplingMeasurementRepository repository;

    private final int pageSize;

    CouplingMeasurementService(CouplingCounter counter, CouplingMeasurementRepository repository,
                               @Value("${application.fitness-function.measurement.pagination.page-size}") int pageSize) {
        this.counter = counter;
        this.repository = repository;
        this.pageSize = pageSize;
    }

    Page<CouplingMeasurement> retrieve(int pageNo) {
        return repository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @Override
    public void notify(CanvasOperation operation) {
        try {
            var summary = counter.count(operation.getCreatedAt());
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
