package net.adam85w.ddd.boundedcontextcanvas.management.diagram.communication;

import net.adam85w.ddd.boundedcontextcanvas.management.diagram.DiagramEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Set;

@Controller
@RequestMapping("/diagram/communication")
class CommunicationDiagramController {

    private final CommunicationDiagramGenerator generator;

    private final DiagramEncoder diagramEncoder;

    CommunicationDiagramController(CommunicationDiagramGenerator generator, DiagramEncoder diagramEncoder) {
        this.generator = generator;
        this.diagramEncoder = diagramEncoder;
    }

    @GetMapping
    String generate(Model model, @RequestParam(name = "ids", required = false) Set<Long> ids) throws IOException {
        var diagramSource = generator.generate(ids);
        model.addAttribute("source", diagramSource);
        model.addAttribute("encoded", diagramEncoder.encode(diagramSource));
        return "diagram/communication";
    }
}
