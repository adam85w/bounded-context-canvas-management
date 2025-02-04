package net.adam85w.ddd.boundedcontextcanvas.management;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class CanvasOperation {

    @Id
    @SequenceGenerator(name = "canvas_operation_seq_generator", sequenceName = "canvas_operation_seq", allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "canvas_operation_seq_generator")
    private Long id;

    private String canvasName;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    private LocalDateTime createdAt;

    private boolean processed;

    protected CanvasOperation() {}

    CanvasOperation(String canvasName, OperationType operationType, LocalDateTime createdAt) {
        this.canvasName = canvasName;
        this.operationType = operationType;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void update(boolean processed) {
        this.processed = processed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CanvasOperation operation)) return false;
        return Objects.equals(canvasName, operation.canvasName) && operationType == operation.operationType && Objects.equals(createdAt, operation.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canvasName, operationType, createdAt);
    }
}
