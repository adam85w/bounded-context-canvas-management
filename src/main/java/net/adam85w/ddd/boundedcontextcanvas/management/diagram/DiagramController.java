package net.adam85w.ddd.boundedcontextcanvas.management.diagram;

import net.sourceforge.plantuml.code.TranscoderUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/diagrams")
class DiagramController {

    private final DiagramGenerator generator;

    DiagramController(DiagramGenerator generator) {
        this.generator = generator;
    }

    @GetMapping
    ResponseEntity<String> generate(@RequestParam(name = "integration", defaultValue = "true") boolean integration) throws IOException {
        var diagramSource = generator.generate();
        if (integration) {
            return ResponseEntity.ok(TranscoderUtil.getDefaultTranscoder().encode(diagramSource));
        } else {
            return ResponseEntity.ok(diagramSource);
        }
    }
}
