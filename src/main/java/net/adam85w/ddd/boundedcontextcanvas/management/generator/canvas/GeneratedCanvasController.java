package net.adam85w.ddd.boundedcontextcanvas.management.generator.canvas;

import jakarta.validation.ValidationException;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/canvases")
class GeneratedCanvasController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratedCanvasController.class);

    private final GeneratedCanvasService service;

    GeneratedCanvasController(GeneratedCanvasService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<String> generate(@RequestParam(name = "templateType", defaultValue = "HTML") String templateType,
                                           @RequestParam(name = "templateName", defaultValue = "modern") String templateName,
                                           @RequestBody BoundedContext boundedContext) throws Exception {
        String key = service.generate(templateType, templateName, boundedContext);
        return new ResponseEntity<>(key, HttpStatus.OK);
    }

    @PutMapping
    ResponseEntity<String> generate(@RequestParam(name = "templateType", defaultValue = "HTML") String templateType,
                                           @RequestParam(name = "templateName", defaultValue = "modern")String templateName,
                                           @RequestParam(name = "id") long id) throws Exception {
        String key = service.generate(templateType, templateName, id);
        return new ResponseEntity<>(key, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<byte[]> get(@RequestParam(name = "key") String key) {
        var generatedCanvas = service.obtainGeneratedCanvas(key);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(generatedCanvas.contentType()));
        return new ResponseEntity<>(generatedCanvas.content(), headers, HttpStatus.OK);
    }

    @ExceptionHandler(ValidationException.class)
    ErrorResponse handleValidationException(ValidationException e) {
        LOGGER.warn(e.getMessage(), e);
        return ErrorResponse.builder(e, HttpStatusCode.valueOf(400), e.getMessage()).build();
    }

    @ExceptionHandler(Exception.class)
    ErrorResponse handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return ErrorResponse.builder(e, HttpStatusCode.valueOf(500), "Internal server error").build();
    }
}
