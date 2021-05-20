package info.navnoire.recipeappserver_springboot.job;

import info.navnoire.recipeappserver_springboot.config.SchedulerConfig;
import info.navnoire.recipeappserver_springboot.service.CategoryService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Victoria Berezina on 18/05/2021 in RecipeAppServer_SpringBoot project
 */
@Component
public class CategoryTreeScrapeJob implements Job {
    private final CategoryService categoryService;

    @Autowired
    public CategoryTreeScrapeJob(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public static final Logger LOG = LoggerFactory.getLogger(CategoryTreeScrapeJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOG.info("Job ** {} ** starting @ {} in {}", context.getJobDetail().getKey().getName(), context.getFireTime(), Thread.currentThread().getName());
        try {
            categoryService.scrapeAllCategories();
        } catch (IOException ioe) {
            SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().forJob(context.getJobDetail())
                    .withIdentity("rescheduleTrigger")
                    .startAt(new Date(System.currentTimeMillis() + (3 * 60 * 1000)))
                    .build();
            try {
                context.getScheduler().rescheduleJob(context.getTrigger().getKey(), trigger);
                LOG.info("Job ** {} ** execution failed.  Reschedule @ {}", context.getJobDetail().getKey().getName(), trigger.getStartTime());
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            throw  new JobExecutionException(ioe.getMessage());
        }
        LOG.info("Job ** {} ** completed successfully.", context.getJobDetail().getKey().getName());
    }
}
