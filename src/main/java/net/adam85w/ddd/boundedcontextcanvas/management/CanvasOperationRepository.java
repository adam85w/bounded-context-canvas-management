package net.adam85w.ddd.boundedcontextcanvas.management;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
interface CanvasOperationRepository extends CrudRepository<CanvasOperation, Long> {

    Set<CanvasOperation> findAllByProcessedIsFalseOrderByCreatedAtAsc();

    Set<CanvasOperation> findAllByProcessedIsTrue();
}
