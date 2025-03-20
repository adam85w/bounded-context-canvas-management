package net.adam85w.ddd.boundedcontextcanvas.management;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CanvasOperationRepository extends CrudRepository<CanvasOperation, Long> {

    CanvasOperation[] findAllByProcessedIsFalseOrderByCreatedAtAsc();
}
