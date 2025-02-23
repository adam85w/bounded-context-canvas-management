package net.adam85w.ddd.boundedcontextcanvas.management;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CanvasRepository extends CrudRepository<Canvas, Long> {

    Iterable<Canvas> findAllByNameContainingOrderByUpdatedAtDesc(String searchPhrase);

    boolean existsByName(String name);

    Iterable<? extends BoundedContextAware> findAllByOrderByUpdatedAtDesc();

    Optional<Canvas> findByName(String name);
}
