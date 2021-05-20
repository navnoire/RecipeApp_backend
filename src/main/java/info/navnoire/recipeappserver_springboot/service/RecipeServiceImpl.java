package info.navnoire.recipeappserver_springboot.service;

import info.navnoire.recipeappserver_springboot.domain.Recipe;
import info.navnoire.recipeappserver_springboot.repository.RecipeRepository;
import info.navnoire.recipeappserver_springboot.service.scraper.RecipeScraper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.quartz.JobExecutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * Created by Victoria Berezina on 07/05/2021 in RecipesAppServer project
 */
@Transactional
@Service("recipeService")
public class RecipeServiceImpl implements RecipeService {
    private static final Log LOG = LogFactory.getLog(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;
    private final RecipeScraper recipeScraper;

    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeScraper recipeScraper) {
        this.recipeRepository = recipeRepository;
        this.recipeScraper = recipeScraper;
    }

    @Override
    @Transactional(readOnly = true)
    public Recipe findFullRecipeById(int id) {
        Recipe recipe = recipeRepository.findById(id);
        Hibernate.initialize(recipe.getIngredients());
        Hibernate.initialize(recipe.getSteps());
        return recipe;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recipe> findByTitlePage(String title, Pageable pageable) {
        return recipeRepository.findAllByTitleContainsIgnoreCase(title, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recipe> findAllPage(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recipe> findByCategoryPage(int id, Pageable pageable) {
        return recipeRepository.findByCategory(id, pageable);
    }

    @Override
    public void ScrapeRecipesInCategory(int category_id) throws IOException {
        List<Recipe> recipes;
        recipes = recipeScraper.scrapeByCategory(category_id);
        recipeRepository.saveAll(recipes);
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
