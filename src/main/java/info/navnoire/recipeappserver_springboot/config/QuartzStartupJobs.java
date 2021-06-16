package info.navnoire.recipeappserver_springboot.config;

import info.navnoire.recipeappserver_springboot.job.CategoryTreeScrapeJob;
import info.navnoire.recipeappserver_springboot.job.RecipeByCategoryScrapeJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;

/**
 * Created by Victoria Berezina on 18/05/2021 in RecipeAppServer_SpringBoot project
 */
@Component
public class QuartzStartupJobs {
    private static final String CRON_CATEGORY_TREE_SCRAPE = "0 0 2 ? * * *";

    @Bean("categoryTreeScrape")
    public JobDetailFactoryBean jobCategoryTreeScrape() {
        return SchedulerConfig.createJobDetail(CategoryTreeScrapeJob.class, "Category tree scrape job", new JobDataMap());
    }

    @Bean("categoryScrapeTrigger")
    public CronTriggerFactoryBean jobCategoryTreeScrapeTrigger(@Qualifier("categoryTreeScrape") JobDetail jobdetail) {
        return SchedulerConfig.createCronTrigger(jobdetail, CRON_CATEGORY_TREE_SCRAPE, "Category tree scrape trigger");
    }

    @Bean("recipeByCategoryScrape")
    JobDetailFactoryBean jobRecipeByCategoryScrape() {
        JobDataMap data = new JobDataMap();
        data.put("index", 0);
        return SchedulerConfig.createJobDetail(RecipeByCategoryScrapeJob.class, "Recipe by category scrape job", data);
    }
}
