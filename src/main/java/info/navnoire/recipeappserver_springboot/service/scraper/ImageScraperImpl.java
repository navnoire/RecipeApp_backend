package info.navnoire.recipeappserver_springboot.service.scraper;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Component
public class ImageScraperImpl implements ImageScraper {
    private static final String IMAGE_LOCAL_STORAGE_PATH = System.getProperty("user.home") + "/recipeapp/images/";
    private static final String IMAGES_ROOT_URL = "https://gotovim-doma.ru/images/recipe/";


    @Override
    public void scrapeImageToStorage(String imageUrl) throws IOException {
        if (imageUrl == null) return;
        URL url = new URL(IMAGES_ROOT_URL + imageUrl);
        String pathName = IMAGE_LOCAL_STORAGE_PATH + imageUrl;
        File file = new File(pathName);
        FileUtils.copyURLToFile(url, file);
    }

    @Override
    public String getImageStoragePath() {
        return IMAGE_LOCAL_STORAGE_PATH;
    }
}
