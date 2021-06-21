package info.navnoire.recipeappserver_springboot.service;

import info.navnoire.recipeappserver_springboot.domain.recipe.Recipe;
import info.navnoire.recipeappserver_springboot.repository.recipe.RecipeRepository;
import info.navnoire.recipeappserver_springboot.service.scraper.RecipeScraper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by Victoria Berezina on 07/05/2021 in RecipesAppServer project
 */
@Transactional
@Service("recipeService")
public class RecipeServiceImpl implements RecipeService {
    private static final Log LOG = LogFactory.getLog(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;
    private final RecipeScraper recipeScraper;
    private final ImageService imageService;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeScraper recipeScraper, ImageService imageService) {
        this.recipeRepository = recipeRepository;
        this.recipeScraper = recipeScraper;
        this.imageService = imageService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Recipe> findFullRecipeById(int id) {
        return recipeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recipe> findByTitlePage(String title, Pageable pageable) {
        return recipeRepository.findAllByTitleContainsIgnoreCase(title, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<Recipe> findAllPage(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<Recipe> findByCategoryPage(int id, Pageable pageable) {
        return recipeRepository.findByCategory(id, pageable);
    }

    @Override
    public List<Recipe> scrapeRecipesInCategory(int category_id) throws IOException {
        List<Recipe> recipes;
        recipes = recipeScraper.scrapeByCategory(category_id);
        recipes.forEach(this::save);
        return recipes;
    }

    @Override
    public Recipe scrapeSingleRecipe(String url, int id) throws IOException {
        return recipeScraper.scrapeSingleRecipe(url,id);
    }

    @Override
    public void save(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    @Override
    public void delete(int id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
        }
    }
}
