package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.circulardependencies;

import jakarta.persistence.*;
import net.adam85w.ddd.boundedcontextcanvas.management.OperationType;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
class CircularDependencyMeasurement {

    @Id
    @SequenceGenerator(name = "circular_dependency_measurement_seq_generator", sequenceName = "circular_dependency_measurement_seq", allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "circular_dependency_measurement_seq_generator")
    private Long id;

    @Column(nullable = false)
    private String canvasName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operationType;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected CircularDependencyMeasurement() {
    }

    CircularDependencyMeasurement(String canvasName, OperationType operationType, int amount, LocalDateTime createdAt) {
        this.canvasName = canvasName;
        this.operationType = operationType;
        this.amount = amount;
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

    public int getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CircularDependencyMeasurement that)) return false;
        return Objects.equals(canvasName, that.canvasName) && operationType == that.operationType && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canvasName, operationType, createdAt);
    }
}
