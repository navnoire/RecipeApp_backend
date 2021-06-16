package info.navnoire.recipeappserver_springboot.service.scraper;

import info.navnoire.recipeappserver_springboot.domain.Recipe;

import java.io.IOException;
import java.util.List;

/**
 * Created by Victoria Berezina on 14/05/2021 in RecipeAppServer_SpringBoot project
 */
public interface RecipeScraper extends Scraper {
    Recipe scrapeSingleRecipe(String url, int id) throws IOException;
    List<Recipe> scrapeByCategory(int category_id) throws IOException;
}
