package info.navnoire.recipeappserver_springboot.config;

import org.apache.commons.lang3.ArrayUtils;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class SchedulerConfiguration {
    private final ApplicationContext applicationContext;

    public SchedulerConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static final Logger LOG = LoggerFactory.getLogger(SchedulerConfiguration.class);

    @Bean
    public JobFactory jobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean(destroyMethod = "destroy")
    public SchedulerFactoryBean scheduler(Trigger trigger, JobDetail... jobs) throws Exception {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory());
        factory.setWaitForJobsToCompleteOnShutdown(false);
        factory.setQuartzProperties(quartzProperties());
        if (ArrayUtils.isNotEmpty(jobs)) {
            factory.setJobDetails(jobs);
        }
        factory.setTriggers(trigger);
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    static JobDetailFactoryBean createJobDetail(Class<? extends Job> jobClass, String jobName, JobDataMap map) {
        LOG.debug("createJobDetail(jobClass={}, jobName={})", jobClass.getName(), jobName);
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setName(jobName);
        factoryBean.setJobDataMap(map);
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(true);
        return factoryBean;
    }

    static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression, String triggerName) {
        LOG.debug("Called createTrigger(jobDetail={}, cronExpr={},triggerName={}", jobDetail.toString(), cronExpression, triggerName);
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setName(triggerName);
        factoryBean.setStartDelay(0L);
        factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        return factoryBean;
    }

}
