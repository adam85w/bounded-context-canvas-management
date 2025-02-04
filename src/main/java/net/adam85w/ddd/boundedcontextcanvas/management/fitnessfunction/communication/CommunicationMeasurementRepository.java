package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.communication;

import org.springframework.data.repository.CrudRepository;

interface CommunicationMeasurementRepository extends CrudRepository<CommunicationMeasurement, Long> {

    Iterable<CommunicationMeasurement> findAllByOrderByCreatedAtDesc();
}
