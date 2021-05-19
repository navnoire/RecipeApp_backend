package info.navnoire.recipeappserver_springboot.controller;

import info.navnoire.recipeappserver_springboot.domain.Recipe;
import info.navnoire.recipeappserver_springboot.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
    public Page<Recipe> listAll(Pageable pageable) {
        return recipeService.findAllPage(pageable);
    }

    @GetMapping(path = "/{id}")
    public Recipe findById(@PathVariable("id") int id) {
        return recipeService.findFullRecipeById(id);
    }

    @GetMapping(path = "/search/{title}")
    public Page<Recipe> findByTitle(@PathVariable("title")String title, Pageable pageable) {
        return recipeService.findByTitlePage(title, pageable);
    }

    @GetMapping(path = "/category/{id}")
    public Page<Recipe> findByCategory(@PathVariable("id") int category_id, Pageable pageable) {
        return recipeService.findByCategoryPage(category_id, pageable);
    }
}
