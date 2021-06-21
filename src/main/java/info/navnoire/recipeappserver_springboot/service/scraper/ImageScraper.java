package info.navnoire.recipeappserver_springboot.service.scraper;

import java.io.IOException;

/**
 * Created by Victoria Berezina on 26/05/2021 in RecipeApp project
 */
public interface ImageScraper {
    void scrapeImageToStorage(String imageUrl) throws IOException;
    String getImageStoragePath();
}
