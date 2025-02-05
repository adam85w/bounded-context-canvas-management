package net.adam85w.ddd.boundedcontextcanvas.management;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Map;

@Controller
class CanvasController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CanvasController.class);

    private static final BoundedContext BOUNDED_CONTEXT_EMPTY = new BoundedContext(null, null, null, null, new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), new LinkedList<>());

    private final BoundedContext boundedContextExample;

    private final CanvasService service;

    private final TemplateObtainer templateObtainer;

    private final ObjectMapper mapper;

    CanvasController(CanvasService service, TemplateObtainer templateObtainer, ObjectMapper mapper, BoundedContext boundedContextExample) {
        this.service = service;
        this.templateObtainer = templateObtainer;
        this.mapper = mapper;
        this.boundedContextExample = boundedContextExample;
    }

    @GetMapping({"/list", "/"})
    ModelAndView list(@RequestParam(name="searchPhrase", required = false) String searchPhrase, @RequestParam(name="action", defaultValue = "EMPTY") Action action, @RequestParam(name="name", required = false) String canvasName, ModelAndView modelAndView) {
        if (Action.EMPTY != action) {
            modelAndView.addObject("action", action);
            modelAndView.addObject("canvasName", new String(Base64.getDecoder().decode(canvasName)));
        }
        if (searchPhrase != null && !searchPhrase.isBlank()) {
            modelAndView.addObject("canvases", service.obtain(searchPhrase));
            modelAndView.addObject("searchPhrase", searchPhrase);
        } else {
            modelAndView.addObject("canvases", service.obtainAll());
            modelAndView.addObject("searchPhrase", null);
        }
        modelAndView.addObject("templates", templateObtainer.obtain());
        modelAndView.setViewName("canvas/list");
        return modelAndView;
    }

    @GetMapping("/add")
    ModelAndView add(@RequestParam(name="example", defaultValue = "false") boolean example, ModelAndView modelAndView) {
        modelAndView.addObject("id", 0);
        modelAndView.addObject("title", "Adding a new Bounded Context Canvas");
        if (example) {
            modelAndView.addObject("boundedContext", boundedContextExample);
        } else {
            modelAndView.addObject("boundedContext", BOUNDED_CONTEXT_EMPTY);
        }
        modelAndView.addObject("templates", templateObtainer.obtain());
        modelAndView.setViewName("canvas/form");
        return modelAndView;
    }

    @GetMapping("/edit")
    ModelAndView edit(ModelAndView modelAndView, @RequestParam(name = "id") long id) throws JsonProcessingException {
        Canvas canvas = (Canvas) service.obtain(id);
        modelAndView.addObject("id", canvas.getId());
        modelAndView.addObject("version", canvas.getVersion());
        modelAndView.addObject("name", canvas.getName());
        modelAndView.addObject("boundedContext", mapper.readValue(canvas.retrieveContext(), BoundedContext.class));
        modelAndView.addObject("templates", templateObtainer.obtain());
        modelAndView.setViewName("canvas/form");
        return modelAndView;
    }

    @GetMapping("/clone")
    ModelAndView clone(ModelAndView modelAndView, @RequestParam(name = "id") long id) throws JsonProcessingException {
        Canvas canvas = (Canvas) service.obtain(id);
        modelAndView.addObject("id", 0);
        modelAndView.addObject("name", canvas.getName());
        modelAndView.addObject("boundedContext", mapper.readValue(canvas.retrieveContext(), BoundedContext.class));
        modelAndView.addObject("templates", templateObtainer.obtain());
        modelAndView.setViewName("canvas/form");
        return modelAndView;
    }

    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<String, String>> save(@RequestBody BoundedContext boundedContext, @RequestParam(name = "id", defaultValue = "0") long id, @RequestParam(name="version", defaultValue = "0") int version, HttpServletRequest request) throws IOException {
        Canvas canvas = service.save(id, version, boundedContext);
        var action = id == 0 ? Action.ADD: Action.EDIT;
        return ResponseEntity.ok(Map.of("url", String.format("%s/list?action=%s&name=%s", request.getContextPath(), action, Base64.getEncoder().encodeToString(canvas.getName().getBytes(StandardCharsets.UTF_8)))));
    }

    @GetMapping(value = {"/remove", "/delete"})
    void delete(@RequestParam("id") long id, @RequestParam("version") int version, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Canvas canvas = (Canvas) service.obtain(id);
        service.delete(id, version);
        response.sendRedirect(String.format("%s/list?action=%s&name=%s", request.getContextPath(), Action.DELETE, Base64.getEncoder().encodeToString(canvas.getName().getBytes(StandardCharsets.UTF_8))));
    }

    @ExceptionHandler(ValidationException.class)
    ErrorResponse handleValidationException(ValidationException e) {
        LOGGER.warn(e.getMessage(), e);
        return ErrorResponse.builder(e, HttpStatusCode.valueOf(400), e.getMessage()).build();
    }

    @ExceptionHandler(CanvasNotFoundException.class)
    ErrorResponse handleEntityNotFoundException(EntityNotFoundException e) {
        LOGGER.warn(e.getMessage(), e);
        return ErrorResponse.builder(e, HttpStatusCode.valueOf(404), e.getMessage()).build();
    }

    @ExceptionHandler(CanvasOperationConflictException.class)
    ErrorResponse handleOptimisticLockException(OptimisticLockException e) {
        LOGGER.warn(e.getMessage(), e);
        return ErrorResponse.builder(e, HttpStatusCode.valueOf(409), e.getMessage()).build();
    }

    @ExceptionHandler(Exception.class)
    ErrorResponse handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return ErrorResponse.builder(e, HttpStatusCode.valueOf(500), "Internal server error").build();
    }

    enum Action {
        ADD("added"), EDIT("edited"), DELETE("deleted"), EMPTY(null);

        final String name;

        Action(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
