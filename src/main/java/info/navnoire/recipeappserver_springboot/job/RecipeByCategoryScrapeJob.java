package info.navnoire.recipeappserver_springboot.job;

import info.navnoire.recipeappserver_springboot.service.RecipeService;
import info.navnoire.recipeappserver_springboot.service.scraper.RecipeScraper;
import org.aspectj.bridge.MessageUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by Victoria Berezina on 18/05/2021 in RecipeAppServer_SpringBoot project
 */
@Component
public class RecipeByCategoryScrapeJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(RecipeByCategoryScrapeJob.class);
    private final RecipeService recipeService;

    public RecipeByCategoryScrapeJob(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public void execute(JobExecutionContext context) {
        LOG.info("Job ** {} ** starting @ {} in {}", context.getJobDetail().getKey().getName(), context.getFireTime(), Thread.currentThread().getName());
       int category_id = context.getTrigger().getJobDataMap().getInt("category_id");
        try {
            recipeService.ScrapeRecipesInCategory(category_id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
    }
}
