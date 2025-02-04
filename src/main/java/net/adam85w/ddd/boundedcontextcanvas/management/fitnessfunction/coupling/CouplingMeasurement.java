package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.coupling;

import jakarta.persistence.*;
import net.adam85w.ddd.boundedcontextcanvas.management.OperationType;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
class CouplingMeasurement {

    @Id
    @SequenceGenerator(name = "coupling_measurement_seq_generator", sequenceName = "coupling_measurement_seq", allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coupling_measurement_seq_generator")
    private Long id;

    @Column(nullable = false)
    private String canvasName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operationType;

    @Column(nullable = false)
    private int componentsAmount;

    @Column(nullable = false)
    private int afferentCouplingsAmount;

    @Column(nullable = false)
    private int efferentCouplingsAmount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected CouplingMeasurement() {
    }

    CouplingMeasurement(String canvasName, OperationType operationType, int componentsAmount, int afferentCouplingsAmount, int efferentCouplingsAmount, LocalDateTime createdAt) {
        this.canvasName = canvasName;
        this.operationType = operationType;
        this.componentsAmount = componentsAmount;
        this.afferentCouplingsAmount = afferentCouplingsAmount;
        this.efferentCouplingsAmount = efferentCouplingsAmount;
        this.createdAt = createdAt;
    }

    public String getCanvasName() {
        return canvasName;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public int getComponentsAmount() {
        return componentsAmount;
    }

    public int getAfferentCouplingsAmount() {
        return afferentCouplingsAmount;
    }

    public int getEfferentCouplingsAmount() {
        return efferentCouplingsAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CouplingMeasurement that)) return false;
        return Objects.equals(canvasName, that.canvasName) && operationType == that.operationType && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canvasName, operationType, createdAt);
    }
}
