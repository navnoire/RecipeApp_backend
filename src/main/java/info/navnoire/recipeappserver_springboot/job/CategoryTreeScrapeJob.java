package info.navnoire.recipeappserver_springboot.job;

import info.navnoire.recipeappserver_springboot.service.CategoryService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class CategoryTreeScrapeJob implements Job {
    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
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
                    .withIdentity("rescheduleTrigger", "RESCHEDULER")
                    .startAt(new Date(System.currentTimeMillis() + (2 * 60 * 1000)))
                    .build();
            try {
                if (context.getTrigger().getKey().getName().equals("Category tree scrape trigger")) {
                    context.getScheduler().triggerJob(context.getJobDetail().getKey());

                } else {
                    context.getScheduler().rescheduleJob(context.getTrigger().getKey(), trigger);
                    LOG.info("Job ** {} ** execution failed.  Reschedule @ {}", context.getJobDetail().getKey().getName(), trigger.getNextFireTime());
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            throw new JobExecutionException(ioe.getMessage());
        }
        LOG.info("Job ** {} ** completed successfully", context.getJobDetail().getKey().getName());
        try {
            context.getScheduler().triggerJob(new JobKey("Recipe by category scrape job"));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}
