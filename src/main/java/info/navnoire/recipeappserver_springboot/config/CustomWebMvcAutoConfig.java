package info.navnoire.recipeappserver_springboot.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebMvcAutoConfig implements WebMvcConfigurer {

    @Bean
    public Jackson2ObjectMapperBuilder configureObjectMapper() {
        Hibernate5Module jacksonHibernateModule = new Hibernate5Module();
        jacksonHibernateModule.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
        return new Jackson2ObjectMapperBuilder()
                .modulesToInstall(jacksonHibernateModule);
    }
}
