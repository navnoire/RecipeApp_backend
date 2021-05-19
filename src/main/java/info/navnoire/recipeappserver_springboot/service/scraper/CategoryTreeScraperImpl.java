package info.navnoire.recipeappserver_springboot.service.scraper;

import info.navnoire.recipeappserver_springboot.constants.ScraperConstants;
import info.navnoire.recipeappserver_springboot.domain.Category;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Victoria Berezina on 01/05/2021 in Scrapers project
 */
@Service
public class CategoryTreeScraperImpl implements CategoryTreeScraper {

    public List<Category> scrapeAll() throws IOException {
        List<Category> initialCategories = fetchCategories(ScraperConstants.CATEGORY_ROOT_URL, true);
        Queue<String> urlQueue = new PriorityQueue<>();
        urlQueue.addAll(getUrls(initialCategories));

        while (!urlQueue.isEmpty()) {
            List<Category> newCategories = fetchCategories(urlQueue.poll(), false);
            urlQueue.addAll(getUrls(newCategories));
            initialCategories.addAll(newCategories);
            System.out.println("Queue = " + urlQueue.size() + " elements");
        }
        return initialCategories;
    }

    private List<String> getUrls(List<Category> categories) {
        List<String> urls=new ArrayList<>();
        for(Category c : categories) {
            urls.add(c.getCategory_url());
        }
        return urls;
    }

    private List<Category> fetchCategories(String url, boolean isInitial) throws IOException {
        List<Category> categories = new ArrayList<>();

        Document document = getDocument(url);

        Element main;
        if (isInitial) {
            main = document.getElementsByClass("menu-main").first().select("tr").first();
        } else {
            main = document.getElementsByClass("razdel-menu").first();
        }
        if (main != null) {
            for (Element cat : main.children().select("a[href]")) {
                Category category = constructCategory(cat);
                category.setParent_id(extractId(url));
                categories.add(category);
            }
        }
        return categories;
    }

    private Category constructCategory(Element categoryElement) {
        Category category = new Category();
        String catUrl = categoryElement.absUrl("href");
        category.setCategory_url(catUrl);
        category.setId(extractId(catUrl));
        category.setTitle(categoryElement.text());
        return category;
    }
}
