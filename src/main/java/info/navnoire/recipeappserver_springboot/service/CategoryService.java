package info.navnoire.recipeappserver_springboot.service;

import info.navnoire.recipeappserver_springboot.domain.recipe.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    Page<Category> findAllPage(Pageable pageable);
    List<Category> findChildCategories(int parent_id);
    void scrapeAllCategories() throws IOException;
    void save(Category category);
}
