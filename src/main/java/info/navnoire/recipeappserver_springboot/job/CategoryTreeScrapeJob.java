package info.navnoire.recipeappserver_springboot.job;

import info.navnoire.recipeappserver_springboot.config.SchedulerConfig;
import info.navnoire.recipeappserver_springboot.service.CategoryService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * Created by Victoria Berezina on 18/05/2021 in RecipeAppServer_SpringBoot project
 */
@Component
public class CategoryTreeScrapeJob extends QuartzJobBean {
    private final CategoryService categoryService;
    @Autowired
    public CategoryTreeScrapeJob(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public static final Logger LOG = LoggerFactory.getLogger(SchedulerConfig.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOG.info("Job ** {} ** starting @ {} in {}", context.getJobDetail().getKey().getName(), context.getFireTime(), Thread.currentThread().getName());
        categoryService.scrapeAllCategories();
        LOG.info("Job ** {} ** completed.  Next job scheduled @ {}", context.getJobDetail().getKey().getName(), context.getNextFireTime());
    }
}
