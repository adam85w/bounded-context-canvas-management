package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.circulardependencies;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CircularDependencyMeasurementRepository extends CrudRepository<CircularDependencyMeasurement, Long>, ListPagingAndSortingRepository<CircularDependencyMeasurement, Long> {
}
