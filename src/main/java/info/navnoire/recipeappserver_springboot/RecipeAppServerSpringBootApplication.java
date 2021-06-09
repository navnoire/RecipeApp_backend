package info.navnoire.recipeappserver_springboot;

import info.navnoire.recipeappserver_springboot.domain.Ingredient;
import info.navnoire.recipeappserver_springboot.domain.Recipe;
import info.navnoire.recipeappserver_springboot.domain.Step;
import info.navnoire.recipeappserver_springboot.service.RecipeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class RecipeAppServerSpringBootApplication {

    public static void main(String[] args) {
      SpringApplication.run(RecipeAppServerSpringBootApplication.class, args);
    }
}

