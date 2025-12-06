package net.adam85w.ddd.boundedcontextcanvas.management;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class CanvasLog implements BoundedContextAware {

    @Id
    @SequenceGenerator(name = "canvas_log_seq_generator", sequenceName = "canvas_log_seq", allocationSize = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "canvas_log_seq_generator")
    private long id;

    private String name;

    @Column(columnDefinition = "BLOB")
    private String context;

    private long canvasOperationId;

    private LocalDateTime createdAt;

    protected CanvasLog() {}

    public CanvasLog(String name, String context, long canvasOperationId,  LocalDateTime createdAt) {
        this.name = name;
        this.context = context;
        this.canvasOperationId = canvasOperationId;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContext() {
        return context;
    }

    public long getCanvasOperationId() {
        return canvasOperationId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String retrieveContext() {
        return context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CanvasLog canvas)) return false;
        return Objects.equals(name, canvas.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
