package info.navnoire.recipeappserver_springboot.service;

import info.navnoire.recipeappserver_springboot.domain.Category;
import info.navnoire.recipeappserver_springboot.domain.Recipe;
import org.quartz.JobExecutionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Victoria Berezina on 09/05/2021 in RecipesAppServer project
 */
public interface CategoryService {
    Page<Category> findAllPage(Pageable pageable);
    List<Category> findChildCategories(int parent_id);
    void scrapeAllCategories() throws JobExecutionException;

    void save(Category category);
    void delete(int id);
}
