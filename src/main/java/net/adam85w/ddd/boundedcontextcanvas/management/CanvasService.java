package net.adam85w.ddd.boundedcontextcanvas.management;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import net.adam85w.ddd.boundedcontextcanvas.model.BoundedContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
class CanvasService implements BoundedContextAwareService {

    private final ObjectMapper mapper;

    private final CanvasRepository repository;

    private final CanvasOperationRepository canvasOperationRepository;

    private final Set<ValidationRule> validationRulesForCreate;

    private final Set<ValidationRule> validationRulesForUpdate;

    CanvasService(CanvasRepository repository, CanvasOperationRepository canvasOperationRepository, ObjectMapper mapper, Set<ValidationRule> validationRules) {
        this.repository = repository;
        this.canvasOperationRepository = canvasOperationRepository;
        this.mapper = mapper;
        this.validationRulesForCreate = validationRules.stream().filter(validationRule -> validationRule.getTypes().contains(ValidationRuleType.CREATE)).collect(Collectors.toSet());
        this.validationRulesForUpdate = validationRules.stream().filter(validationRule -> validationRule.getTypes().contains(ValidationRuleType.UPDATE)).collect(Collectors.toSet());
    }

    @Override
    public BoundedContextAware obtain(long id) {
        return repository.findById(id).orElseThrow(() -> new CanvasNotFoundException("The canvas doesn't exist or was removed."));
    }

    @Override
    public Iterable<? extends BoundedContextAware> obtainAll() {
        return repository.findAll();
    }

    Iterable<? extends BoundedContextAware> obtain(String searchPhrase) {
        return repository.findAllByNameContaining(searchPhrase);
    }

    Canvas save(long id, int version, BoundedContext boundedContext) throws JsonProcessingException {
        Canvas entity;
        if (id == 0) {
            validationRulesForCreate.stream()
                    .filter(validationRule -> validationRule.test(boundedContext))
                    .findFirst()
                    .ifPresent(validationRule -> { throw new ValidationException(validationRule.getMessage()); });
            entity =
            repository.save(new Canvas(boundedContext.getName(), mapper.writeValueAsString(boundedContext), LocalDateTime.now()));
            canvasOperationRepository.save(new CanvasOperation(entity.getName(), OperationType.ADD, LocalDateTime.now()));
        } else {
            validationRulesForUpdate.stream()
                    .filter(validationRule -> validationRule.test(boundedContext))
                    .findFirst()
                    .ifPresent(validationRule -> { throw new ValidationException(validationRule.getMessage()); });
            entity = repository.findById(id).orElseThrow(() -> new CanvasNotFoundException("The canvas doesn't exist or was removed."));
            if (entity.getVersion() != version) {
                throw new CanvasOperationConflictException("The canvas was modified by a different user! Please refresh and check for changes.");
            }
            entity.update(mapper.writeValueAsString(boundedContext));
            repository.save(entity);
            canvasOperationRepository.save(new CanvasOperation(entity.getName(), OperationType.EDIT, LocalDateTime.now()));
        }

        return entity;
    }

    void delete(long id, int version) {
        Canvas entity = repository.findById(id).orElseThrow(() -> new CanvasNotFoundException("The canvas doesn't exist or was removed."));
        if (entity.getVersion() != version) {
            throw new CanvasOperationConflictException("TThe canvas was modified by a different user! Please refresh and check for changes.");
        }
        repository.delete(entity);
        canvasOperationRepository.save(new CanvasOperation(entity.getName(), OperationType.REMOVE, LocalDateTime.now()));
    }
}
