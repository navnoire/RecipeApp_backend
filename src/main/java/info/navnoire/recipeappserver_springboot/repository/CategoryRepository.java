package info.navnoire.recipeappserver_springboot.repository;

import info.navnoire.recipeappserver_springboot.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Victoria Berezina on 09/05/2021 in RecipesAppServer project
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Page<Category> findAll(Pageable pageable);
    Category findById(int id);

    @Query(value = "select c from Category c where  c.parent_id=?1")
    List<Category> findChildCategories(int parent_Id);
}
