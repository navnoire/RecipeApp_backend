package info.navnoire.recipeappserver_springboot.controller;

import info.navnoire.recipeappserver_springboot.domain.Recipe;
import info.navnoire.recipeappserver_springboot.domain.Step;
import info.navnoire.recipeappserver_springboot.service.ImageService;
import info.navnoire.recipeappserver_springboot.service.RecipeService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Victoria Berezina on 09/05/2021 in RecipesAppServer project
 */
@RestController
@RequestMapping("/api/recipe")
public class RecipeApiController {
    private RecipeService recipeService;

    @Autowired
    public void setRecipeService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        Pageable paging = PageRequest.of(page, size);
        Slice<Recipe> pageRecipes = recipeService.findAllPage(paging);
        return getRecipeListResponseEntity(pageRecipes);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Recipe> findById(@PathVariable("id") int id) {
        try {
            Recipe recipe = recipeService.findFullRecipeById(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(recipe, headers, HttpStatus.OK);
        } catch (NoSuchElementException elementException) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<Map<String,Object>> findByCategory(@PathVariable("id") int category_id,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "30") int size) {
        Pageable paging = PageRequest.of(page, size);
        Slice<Recipe> pageRecipes = recipeService.findByCategoryPage(category_id, paging);
        return getRecipeListResponseEntity(pageRecipes);

    }

    private ResponseEntity<Map<String,Object>> getRecipeListResponseEntity(Slice<Recipe> recipes) {
        Map<String, Object> result = new HashMap<>();
        result.put("recipes", recipes.getContent());
        result.put("currentPage", recipes.getNumber());
        result.put("hasNext", recipes.hasNext());
        result.put("hasPrevious", recipes.hasPrevious());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}


