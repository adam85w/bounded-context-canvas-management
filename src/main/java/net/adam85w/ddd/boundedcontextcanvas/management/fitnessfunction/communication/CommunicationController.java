package net.adam85w.ddd.boundedcontextcanvas.management.fitnessfunction.communication;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/fitness-functions/communications")
@ConditionalOnProperty(prefix = "application.fitness-function", value = "enabled", havingValue = "true")
class CommunicationController {

    private final CommunicationMeasurementService measurementService;

    CommunicationController(CommunicationMeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping
    String count(Model model, @RequestParam(name = "pageNo", defaultValue = "0") int pageNo) {
        model.addAttribute("page", measurementService.retrieve(pageNo));
        return "fitness_functions/communications";
    }
}
