package info.navnoire.recipeappserver_springboot.service;

import info.navnoire.recipeappserver_springboot.domain.recipe.Category;
import info.navnoire.recipeappserver_springboot.repository.recipe.CategoryRepository;
import info.navnoire.recipeappserver_springboot.scraper.CategoryTreeScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Transactional
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryTreeScraper categoryTreeScraper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryTreeScraper categoryTreeScraper) {
        this.categoryRepository = categoryRepository;
        this.categoryTreeScraper = categoryTreeScraper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> findAllPage(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findChildCategories(int parent_id) {
        return categoryRepository.findChildCategories(parent_id);
    }

    @Override
    public void scrapeAllCategories() throws IOException {
        List<Category> allCategories;
        allCategories = categoryTreeScraper.scrapeAll();
        allCategories.forEach(this::save);
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

}
