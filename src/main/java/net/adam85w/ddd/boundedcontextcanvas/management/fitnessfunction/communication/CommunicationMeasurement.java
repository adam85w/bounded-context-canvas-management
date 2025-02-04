package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.communication;

import jakarta.persistence.*;
import net.adam85w.ddd.boundedcontextcanvas.management.OperationType;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
class CommunicationMeasurement {

    @Id
    @SequenceGenerator(name = "communication_measurement_seq_generator", sequenceName = "communication_measurement_seq", allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "communication_measurement_seq_generator")
    private Long id;

    @Column(nullable = false)
    private String canvasName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operationType;

    @Column(nullable = false)
    private long queriesAmount;

    @Column(nullable = false)
    private long commandsAmount;

    @Column(nullable = false)
    private long eventsAmount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected CommunicationMeasurement() {}

    CommunicationMeasurement(String canvasName, OperationType operationType, long queriesAmount, long commandsAmount, long eventsAmount, LocalDateTime createdAt) {
        this.canvasName = canvasName;
        this.operationType = operationType;
        this.queriesAmount = queriesAmount;
        this.commandsAmount = commandsAmount;
        this.eventsAmount = eventsAmount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getCanvasName() {
        return canvasName;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public long getCommandsAmount() {
        return commandsAmount;
    }

    public long getQueriesAmount() {
        return queriesAmount;
    }

    public long getEventsAmount() {
        return eventsAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommunicationMeasurement that)) return false;
        return Objects.equals(canvasName, that.canvasName) && operationType == that.operationType && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canvasName, operationType, createdAt);
    }
}
