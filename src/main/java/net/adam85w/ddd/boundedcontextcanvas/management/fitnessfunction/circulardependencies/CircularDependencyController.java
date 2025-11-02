package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.circulardependencies;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/fitness-functions/circular-dependencies")
@ConditionalOnProperty(prefix = "application.fitness-function", value = "enabled", havingValue = "true")
class CircularDependencyController {

    private final CircularDependencyDiscoverer discoverer;

    private final CircularDependencyMeasurementService measurementService;

    private final CircularDependencyPresenter presenter;

    CircularDependencyController(CircularDependencyDiscoverer discoverer, CircularDependencyMeasurementService measurementService, CircularDependencyPresenter presenter) {
        this.discoverer = discoverer;
        this.measurementService = measurementService;
        this.presenter = presenter;
    }

    @GetMapping
    String discover(Model model, @RequestParam(name = "pageNo", defaultValue = "0") int pageNo) throws IOException {
        model.addAttribute("circularDependencies", discoverer.discover(LocalDateTime.now()).stream().map(presenter::create).collect(Collectors.toSet()));
        model.addAttribute("page", measurementService.retrieve(pageNo));
        return "fitness_functions/circular_dependencies";
    }
}
