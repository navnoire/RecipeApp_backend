package info.navnoire.recipeappserver_springboot.config;

import info.navnoire.recipeappserver_springboot.job.CategoryTreeScrapeJob;
import info.navnoire.recipeappserver_springboot.job.RecipeByCategoryScrapeJob;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victoria Berezina on 18/05/2021 in RecipeAppServer_SpringBoot project
 */
@Component
public class QuartzStartupJobs {
    private static final String CRON_EVERY_FIVE_MINUTES = "0 0/5 * ? * * *";
    private static final String CRON_EVERY_THIRTY_MINUTES = "0 0/30 * ? * * *";
    private static final String CRON_EVERY_THREE_HOURS_START_AT_16 = "0 0 16/3 ? * * *";
    private static final String CRON_EVERY_DAY_AT_7_AM = "0 0 7 * * ?";
    private static final String CRON_EVERY_DAY_AT_8_AM = "0 0 8 * * ?";
    private static final String CRON_EVERY_DAY_AT_MIDNIGHT = "0 0 0 * * ?";

    @Bean("categoryTreeScrape")
    public JobDetailFactoryBean jobCategoryTreeScrape() {
        return SchedulerConfig.createJobDetail(CategoryTreeScrapeJob.class, "Category tree scrape job");
    }

    @Bean("categoryScrapeTrigger")
    public CronTriggerFactoryBean jobCategoryTreeScrapeTrigger(@Qualifier("categoryTreeScrape") JobDetail jobdetail) {
        return SchedulerConfig.createCronTrigger(jobdetail, CRON_EVERY_DAY_AT_7_AM, "Category tree scrape trigger");
    }

    @Bean("recipeByCategoryScrape")
    JobDetailFactoryBean jobRecipeByCategoryScrape() {
        return SchedulerConfig.createJobDetail(RecipeByCategoryScrapeJob.class, "Recipe by category scrape job");
    }

    @Bean
    public CronTriggerFactoryBean jobScrapeRecipesInCategory1(@Qualifier("recipeByCategoryScrape") JobDetail jobDetail) {
        Map<String, Integer> data = new HashMap<>();
        data.put("category_id", 1);
       return SchedulerConfig.createCronTriggerWithData(jobDetail, CRON_EVERY_THREE_HOURS_START_AT_16, "Recipe by category scrape trigger", data);
    }
}
