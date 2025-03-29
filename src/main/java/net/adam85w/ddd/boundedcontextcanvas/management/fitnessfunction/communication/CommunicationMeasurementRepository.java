package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.communication;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

interface CommunicationMeasurementRepository extends CrudRepository<CommunicationMeasurement, Long>, ListPagingAndSortingRepository<CommunicationMeasurement, Long> {
}
