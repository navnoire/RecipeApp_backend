package info.navnoire.recipeappserver_springboot.repository.recipe;

import info.navnoire.recipeappserver_springboot.domain.recipe.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Page<Category> findAll(Pageable pageable);
    Category findById(int id);

    @Query(value = "select c from Category c where  c.parent_id=?1")
    List<Category> findChildCategories(int parent_Id);
}
