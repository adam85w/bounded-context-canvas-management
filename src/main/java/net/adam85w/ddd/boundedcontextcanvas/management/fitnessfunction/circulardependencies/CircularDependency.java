package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.circulardependencies;

import java.util.List;

public record CircularDependency(String title, List<Relation> chain, String diagram) {
}
