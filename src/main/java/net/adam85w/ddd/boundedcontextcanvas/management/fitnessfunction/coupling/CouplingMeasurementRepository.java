package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.coupling;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

interface CouplingMeasurementRepository extends CrudRepository<CouplingMeasurement, Long>, ListPagingAndSortingRepository<CouplingMeasurement, Long> {
}
