package info.navnoire.recipeappserver_springboot.repository.recipe;

import info.navnoire.recipeappserver_springboot.domain.recipe.Step;
import org.springframework.data.repository.CrudRepository;

public interface StepRepository extends CrudRepository<Step, Long> {
}
