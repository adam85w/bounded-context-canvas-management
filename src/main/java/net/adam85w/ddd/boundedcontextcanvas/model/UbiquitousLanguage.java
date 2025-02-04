package net.adam85w.ddd.boundedcontextcanvas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class UbiquitousLanguage {

    @NotBlank
    @Schema(name = "name", description = "Context-specific domain terminology.", example = "Rule Cluster")
    private final String name;

    @NotBlank
    @Schema(name = "description", description = "Description of a context specific domain terminology", example = "Grouping of scoring rules (ko criteria & point based) into clusters that can be independently scored")
    private final String description;

    public UbiquitousLanguage(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
