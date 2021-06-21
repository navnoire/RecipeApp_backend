package info.navnoire.recipeappserver_springboot.repository.recipe;

import info.navnoire.recipeappserver_springboot.domain.recipe.Step;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Victoria Berezina on 01/06/2021 in RecipeApp project
 */
public interface StepRepository extends CrudRepository<Step, Long> {
}
