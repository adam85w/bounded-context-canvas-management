package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.coupling;

import org.springframework.data.repository.CrudRepository;

interface CouplingMeasurementRepository extends CrudRepository<CouplingMeasurement, Long> {

    Iterable<CouplingMeasurement> findAllByOrderByCreatedAtDesc();

}
