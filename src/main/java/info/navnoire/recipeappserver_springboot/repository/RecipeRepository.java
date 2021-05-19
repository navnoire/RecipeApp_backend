package info.navnoire.recipeappserver_springboot.repository;

import info.navnoire.recipeappserver_springboot.domain.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Victoria Berezina on 05/05/2021 in RecipesAppServer project
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    Page<Recipe> findAll(Pageable pageable);

    Recipe findById(int id);

    @Query("select distinct r from recipe r join r.categories c where c.id=?1")
    Page<Recipe> findByCategory(int category_id, Pageable pageable);

    Page<Recipe> findAllByTitleContainsIgnoreCase(String title, Pageable pageable);



}
