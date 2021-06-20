package info.navnoire.recipeappserver_springboot.service;

import info.navnoire.recipeappserver_springboot.constants.ScraperConstants;
import info.navnoire.recipeappserver_springboot.domain.recipe.Recipe;
import info.navnoire.recipeappserver_springboot.domain.recipe.Step;
import info.navnoire.recipeappserver_springboot.service.scraper.ImageScraper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Victoria Berezina on 26/05/2021 in RecipeApp project
 */
@Service
public class ImageService {
    private final ImageScraper imageScraper;

    public ImageService(ImageScraper imageScraper) {
        this.imageScraper = imageScraper;
    }

    public void scrapeImagesFromUrlToStorage(List<Recipe> recipeList) throws IOException {
        for(Recipe recipe : recipeList) {
            List<String> urls = recipe.getSteps().stream().map(Step::getImageUrl).collect(Collectors.toList());
            urls.add(recipe.getMain_image_url());
            for (String url : urls) {
                imageScraper.scrapeImageToStorage(url);
            }
        }
    }

    public FileSystemResource getImageFromFileSystem(String imageUrl) {
        Path path = Paths.get(ScraperConstants.IMAGE_LOCAL_STORAGE_PATH, imageUrl);
        return new FileSystemResource(path);
    }
}
