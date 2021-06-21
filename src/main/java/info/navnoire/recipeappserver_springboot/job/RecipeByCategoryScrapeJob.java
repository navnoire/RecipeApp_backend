package info.navnoire.recipeappserver_springboot.job;

import info.navnoire.recipeappserver_springboot.domain.recipe.Category;
import info.navnoire.recipeappserver_springboot.domain.recipe.Recipe;
import info.navnoire.recipeappserver_springboot.service.CategoryService;
import info.navnoire.recipeappserver_springboot.service.ImageService;
import info.navnoire.recipeappserver_springboot.service.RecipeService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Victoria Berezina on 18/05/2021 in RecipeAppServer_SpringBoot project
 */
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class RecipeByCategoryScrapeJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(RecipeByCategoryScrapeJob.class);
    private RecipeService recipeService;
    private CategoryService categoryService;
    private ImageService imageService;
    private int index;

    private List<Integer> categoryList ;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
        categoryList = initCategoryList();
    }

    @Autowired
    public void setImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    @Autowired
    public void setRecipeService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        LOG.info("Job ** {} ** starting @ {} in {}", context.getJobDetail().getKey().getName(), context.getFireTime(), Thread.currentThread().getName());
        List<Recipe> newRecipes;
        Scheduler scheduler = context.getScheduler();
        try {
            newRecipes = recipeService.scrapeRecipesInCategory(categoryList.get(index));
            imageService.scrapeImagesFromUrlToStorage(newRecipes);
        } catch (IOException ioe) {
            SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().forJob(context.getJobDetail())
                    .withIdentity("rescheduleTrigger", "RESCHEDULER")
                    .startAt(new Date(System.currentTimeMillis() + (2 * 60 * 1000)))
                    .build();
            try {
                    context.getScheduler().rescheduleJob(context.getTrigger().getKey(), trigger);
                    LOG.info("Job ** {} ** execution failed.  Reschedule @ {}",
                            context.getJobDetail().getKey().getName(), trigger.getNextFireTime());

            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            throw new JobExecutionException(ioe.getMessage());
        }

        LOG.info("Job ** {} ** completed. Category {} scraped, added {} new recipes",
                context.getJobDetail().getKey().getName(), categoryList.get(index), newRecipes.size());
        if(index < categoryList.size() - 1) {
            // if there are categories to scrape
            context.getJobDetail().getJobDataMap().put("index", index + 1);
            try {
                context.getScheduler().triggerJob(context.getJobDetail().getKey());
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        else {
            context.getJobDetail().getJobDataMap().put("index", 0);
            LOG.info("Scraping recipes finished");
        }
    }

    private List<Integer> initCategoryList() {
        return categoryService.findChildCategories(0).stream().map(Category::getId).collect(Collectors.toList());
    }
}
