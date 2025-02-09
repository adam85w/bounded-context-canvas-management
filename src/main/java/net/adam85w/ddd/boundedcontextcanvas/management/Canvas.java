package net.adam85w.ddd.boundedcontextcanvas.management;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
class Canvas implements BoundedContextAware {

    @Id
    @SequenceGenerator(name = "canvas_seq_generator", sequenceName = "canvas_seq", allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "canvas_seq_generator")
    private Long id;

    @Version
    private int version;

    private String name;

    @Column(columnDefinition = "BLOB")
    private String context;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String retrieveContext() {
        return context;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public int getVersion() {
        return version;
    }

    protected Canvas() {
    }

    Canvas(String name, String context, LocalDateTime createdAt) {
        this.name = name;
        this.context = context;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    public void update(String context, LocalDateTime updatedAt) {
        this.context = context;
        this.updatedAt = updatedAt;
        version++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Canvas canvas)) return false;
        return Objects.equals(name, canvas.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
