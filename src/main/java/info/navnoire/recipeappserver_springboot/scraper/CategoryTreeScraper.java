package info.navnoire.recipeappserver_springboot.scraper;

import info.navnoire.recipeappserver_springboot.domain.recipe.Category;

import java.io.IOException;
import java.util.List;

public interface CategoryTreeScraper extends Scraper {
    List<Category> scrapeAll() throws IOException;
}
