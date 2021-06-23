package info.navnoire.recipeappserver_springboot.scraper;

import info.navnoire.recipeappserver_springboot.domain.recipe.Recipe;

import java.io.IOException;
import java.util.List;

public interface RecipeScraper extends Scraper {
    Recipe scrapeSingleRecipe(String url, int id) throws IOException;
    List<Recipe> scrapeByCategory(int category_id) throws IOException;
}
