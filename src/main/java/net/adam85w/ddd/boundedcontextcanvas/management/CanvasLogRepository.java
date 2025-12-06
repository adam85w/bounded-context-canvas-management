package net.adam85w.ddd.boundedcontextcanvas.management;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
interface CanvasLogRepository extends CrudRepository<CanvasLog, Long> {

    Iterable<? extends BoundedContextAware> findByCanvasOperationId(long canvasOperationId);

    void deleteAllByCanvasOperationIdIn(Set<Long> canvasOperationIds);
}

