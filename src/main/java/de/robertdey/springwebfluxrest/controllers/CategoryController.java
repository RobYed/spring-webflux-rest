package de.robertdey.springwebfluxrest.controllers;

import de.robertdey.springwebfluxrest.domain.Category;
import de.robertdey.springwebfluxrest.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(CategoryController.API_PATH)
public class CategoryController {

    static final String API_PATH = "/api/v1/categories";

    private final CategoryRepository categoryRepository;

    @GetMapping("/")
    Flux<Category> list() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<Category> getById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    Mono<Void> create(@RequestBody Publisher<Category> categoryPublisher) {
        return categoryRepository.saveAll(categoryPublisher).then();
    }

    @PutMapping("/{id}")
    Mono<Category> update(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("/{id}")
    Mono<Category> patch(@PathVariable String id, @RequestBody Category category) {
        return categoryRepository.findById(id)
                .flatMap(foundCategory -> {
                    if (!category.getDescription().equals(foundCategory.getDescription())) {
                        foundCategory.setDescription(category.getDescription());
                        return categoryRepository.save(foundCategory);
                    }
                    return Mono.just(foundCategory);
                });
    }
}
