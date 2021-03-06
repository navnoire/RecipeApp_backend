package info.navnoire.recipeappserver_springboot.service;

import info.navnoire.recipeappserver_springboot.domain.recipe.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RecipeService {
    Optional<Recipe> findFullRecipeById(int id);
    Slice<Recipe> findByCategoryPage(int id, Pageable pageable);
    Slice<Recipe> findByTitlePage(String title, Pageable pageable);
    Slice<Recipe> findAllPage(Pageable pageable);

    List<Recipe> scrapeRecipesInCategory(int category_id) throws IOException;
    Recipe scrapeSingleRecipe(String url, int id) throws IOException;

    void save(Recipe recipe);
    void delete(int id);
}
