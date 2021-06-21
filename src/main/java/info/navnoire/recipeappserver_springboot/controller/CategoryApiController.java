package info.navnoire.recipeappserver_springboot.controller;

import info.navnoire.recipeappserver_springboot.domain.recipe.Category;
import info.navnoire.recipeappserver_springboot.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Victoria Berezina on 09/05/2021 in RecipesAppServer project
 */
@RestController
@RequestMapping("/api/category")
public class CategoryApiController {
    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(path = "/child/{id}")
    public List<Category> getChildOf(@PathVariable("id") int id) {
        return categoryService.findChildCategories(id);
    }

    @GetMapping
    public Page<Category> listAll(Pageable pageable) {
        return categoryService.findAllPage(pageable);
    }

}
