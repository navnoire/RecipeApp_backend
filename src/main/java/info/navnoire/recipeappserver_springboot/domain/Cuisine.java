package info.navnoire.recipeappserver_springboot.domain;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Victoria Berezina on 01/05/2021 in recipeApp_spring project
 */

public class Cuisine {
    private Long id;
    private String name;
    private final Set<Recipe> recipes = new HashSet<>();
}
