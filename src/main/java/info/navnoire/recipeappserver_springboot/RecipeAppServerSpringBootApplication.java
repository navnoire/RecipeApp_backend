package info.navnoire.recipeappserver_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class RecipeAppServerSpringBootApplication {

    public static void main(String[] args) {
      SpringApplication.run(RecipeAppServerSpringBootApplication.class, args);
    }
}

