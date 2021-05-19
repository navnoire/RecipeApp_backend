package info.navnoire.recipeappserver_springboot.service.scraper;

import info.navnoire.recipeappserver_springboot.domain.Category;

import java.io.IOException;
import java.util.List;

/**
 * Created by Victoria Berezina on 14/05/2021 in RecipeAppServer_SpringBoot project
 */
public interface CategoryTreeScraper extends Scraper {
    List<Category> scrapeAll() throws IOException;
}
