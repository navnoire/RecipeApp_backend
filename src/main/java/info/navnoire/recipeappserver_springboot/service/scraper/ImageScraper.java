package info.navnoire.recipeappserver_springboot.service.scraper;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static info.navnoire.recipeappserver_springboot.constants.ScraperConstants.IMAGES_ROOT_URL;
import static info.navnoire.recipeappserver_springboot.constants.ScraperConstants.IMAGE_LOCAL_STORAGE_PATH;

/**
 * Created by Victoria Berezina on 16/05/2021 in RecipeAppServer_SpringBoot project
 */
@Component
public class ImageScraper {
    public void scrapeImageToStorage(String imageUrl) throws IOException {
        URL url = new URL(IMAGES_ROOT_URL + imageUrl);
        String pathName = IMAGE_LOCAL_STORAGE_PATH + imageUrl;
        File file = new File(pathName);
        FileUtils.copyURLToFile(url,file);
    }
}
