package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.circulardependencies;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/fitness-functions/circular-dependencies")
class CircularDependencyController {

    private final CircularDependencyDiscoverer discoverer;

    private final CircularDependencyMeasurementService measurementService;

    CircularDependencyController(CircularDependencyDiscoverer discoverer, CircularDependencyMeasurementService measurementService) {
        this.discoverer = discoverer;
        this.measurementService = measurementService;
    }

    @GetMapping
    String discover(Model model) throws IOException {
        model.addAttribute("circularDependencies", discoverer.discover());
        model.addAttribute("measurements", measurementService.retrieve());
        return "fitness_functions/circular_dependencies";
    }
}
