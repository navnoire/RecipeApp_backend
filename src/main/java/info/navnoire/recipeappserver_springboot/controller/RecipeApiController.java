package info.navnoire.recipeappserver_springboot.controller;

import info.navnoire.recipeappserver_springboot.controller.exhandling.ResourceNotFoundException;
import info.navnoire.recipeappserver_springboot.domain.recipe.Recipe;
import info.navnoire.recipeappserver_springboot.service.RecipeService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class RecipeApiController {
    private RecipeService recipeService;

    @Autowired
    public void setRecipeService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @PreAuthorize(value = "hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        Pageable paging = PageRequest.of(page, size);
        Slice<Recipe> pageRecipes = recipeService.findAllPage(paging);
        return getRecipeListResponseEntity(pageRecipes);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Recipe> findById(@PathVariable("id") int id) {
        Recipe recipe = recipeService.findFullRecipeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found recipe with id " + id));
        Hibernate.initialize(recipe.getIngredients());
        Hibernate.initialize(recipe.getSteps());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(recipe, headers, HttpStatus.OK);

    }

    @GetMapping(path = "/search/{title}")
    public ResponseEntity<Map<String, Object>> findByTitle(@PathVariable("title") String title,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "30") int size) {
        Pageable paging = PageRequest.of(page, size);
        Slice<Recipe> pageRecipes = recipeService.findByTitlePage(title, paging);
        return getRecipeListResponseEntity(pageRecipes);
    }

    @GetMapping(path = "/category/{id}")
    public ResponseEntity<Map<String, Object>> findByCategory(@PathVariable("id") int category_id,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "30") int size) {
        Pageable paging = PageRequest.of(page, size);
        Slice<Recipe> pageRecipes = recipeService.findByCategoryPage(category_id, paging);
        return getRecipeListResponseEntity(pageRecipes);

    }

    private ResponseEntity<Map<String, Object>> getRecipeListResponseEntity(Slice<Recipe> recipes) {
        Map<String, Object> result = new HashMap<>();
        result.put("recipes", recipes.getContent());
        result.put("currentPage", recipes.getNumber());
        result.put("hasNext", recipes.hasNext());
        result.put("hasPrevious", recipes.hasPrevious());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}


