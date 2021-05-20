package info.navnoire.recipeappserver_springboot.service;

import info.navnoire.recipeappserver_springboot.domain.Recipe;
import org.quartz.JobExecutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

/**
 * Created by Victoria Berezina on 07/05/2021 in RecipesAppServer project
 */
public interface RecipeService {
    Recipe findFullRecipeById(int id);
    Page<Recipe> findByCategoryPage(int id, Pageable pageable);
    Page<Recipe> findByTitlePage(String title, Pageable pageable);
    Page<Recipe> findAllPage(Pageable pageable);

    void ScrapeRecipesInCategory(int category_id) throws IOException;

    void save(Recipe recipe);
    void delete(int id);
}
