package info.navnoire.recipeappserver_springboot.config;

import info.navnoire.recipeappserver_springboot.constants.ScraperConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebMvcAutoConfig implements WebMvcConfigurer {

    String externalImageStorage = "file:" + ScraperConstants.IMAGE_LOCAL_STORAGE_PATH;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations(externalImageStorage);
    }
}
