package info.navnoire.recipeappserver_springboot.repository;

import info.navnoire.recipeappserver_springboot.domain.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Victoria Berezina on 05/05/2021 in RecipesAppServer project
 */
@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Integer> {
    Slice<Recipe> findAll(Pageable pageable);

    Recipe findById(int id);

    @Query("select distinct r from recipe r join r.categories c where c.id=?1")
    Slice<Recipe> findByCategory(int category_id, Pageable pageable);

    Page<Recipe> findAllByTitleContainsIgnoreCase(String title, Pageable pageable);



}
