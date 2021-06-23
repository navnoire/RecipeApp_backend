package info.navnoire.recipeappserver_springboot.scraper;

import java.io.IOException;

public interface ImageScraper {
    void scrapeImageToStorage(String imageUrl) throws IOException;
    String getImageStoragePath();
}
