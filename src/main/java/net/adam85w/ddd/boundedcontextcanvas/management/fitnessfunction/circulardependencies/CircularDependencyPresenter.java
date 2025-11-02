package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.circulardependencies;

import net.adam85w.ddd.boundedcontextcanvas.management.diagram.DiagramEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class CircularDependencyPresenter {

    private final DiagramEncoder diagramEncoder;

    CircularDependencyPresenter(DiagramEncoder diagramEncoder) {
        this.diagramEncoder = diagramEncoder;
    }

    CircularDependency create(List<Relation> chain) {
        return new CircularDependency(createTitle(chain), chain, createDiagram(chain));
    }

    private String createTitle(List<Relation> chain) {
        if (chain.size() == 3) {
            return chain.getFirst().componentA() + " -> " + chain.getFirst().componentB() + " -> " +
                    chain.getLast().componentA() + " -> " + chain.getLast().componentB();
        }
        return chain.getFirst().componentA() + " -> " + chain.getFirst().componentB() + " -> ... -> " +
                chain.get(chain.size()-2).componentA() + " -> " + chain.get(chain.size()-2).componentB();
    }

    private String createDiagram(List<Relation> chain) {
        var last = chain.removeLast();
        StringBuilder builder = new StringBuilder();
        builder.append("@startuml\n");
        builder.append("\n\n");
        builder.append("!theme plain\n");
        builder.append("skinparam BackgroundColor transparent\n");
        builder.append("skinparam componentStyle rectangle)\n");
        builder.append("\n\n");
        for (Relation relation : chain) {
            builder.append("component \"" + relation.componentA() + "\" as " + formatComponentName(relation.componentA()) + "\n");
        }
        builder.append("\n\n");
        for (Relation relation : chain) {
            builder.append(formatComponentName(relation.componentA()) + " --> " + formatComponentName(relation.componentB()) + "\n");
        }
        builder.append("\n\n");
        builder.append("@enduml");
        chain.addLast(last);
        return diagramEncoder.encode(builder.toString());
    }

    private String formatComponentName(String name) {
        return name.replaceAll(" ", "_").replaceAll("-", "_");
    }
}
