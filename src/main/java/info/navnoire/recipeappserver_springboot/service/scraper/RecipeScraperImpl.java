package info.navnoire.recipeappserver_springboot.service.scraper;

import info.navnoire.recipeappserver_springboot.domain.Category;
import info.navnoire.recipeappserver_springboot.domain.Ingredient;
import info.navnoire.recipeappserver_springboot.domain.Recipe;
import info.navnoire.recipeappserver_springboot.domain.Step;
import info.navnoire.recipeappserver_springboot.repository.CategoryRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victoria Berezina on 01/05/2021 in Scrapers project
 */
@Service
public class RecipeScraperImpl implements RecipeScraper {
    private static final Log LOG = LogFactory.getLog(RecipeScraperImpl.class);

    private final CategoryRepository categoryRepository;

    public RecipeScraperImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Recipe scrapeSingleRecipe(String url) throws IOException {
        LOG.info("Scraping " + url + " in " + Thread.currentThread().getName());
        Document document = getDocument(url);

        Recipe recipe = new Recipe();
        recipe.setId(extractId(url));
        recipe.setTitle(document.select("h1")
                .text()
                .replaceAll("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]",""));
        recipe.setBody_url(url);

        //ingredients set
        Element ingredientContainer = document.getElementsByClass("recept-table").first();
        for (Element row : ingredientContainer.select("tr")) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(row.select("th").text());
            ingredient.setAmount(row.select("td").text());
            ingredient.setRecipe(recipe);
            recipe.getIngredients().add(ingredient);
        }

        // category list
        Element categoriesContainer = document.getElementsByClass("crubs").first();
        Elements categorySet = categoriesContainer.select("a[href]");
        for (Element e : categorySet) {
            Integer categoryId = extractId(e.attr("href"));
            if (categoryId == 0) continue;
            Category c = new Category();
            c.setId(categoryId);
            recipe.getCategories().add(c);
        }

        //steps
        Element stepContainer = document.getElementsByAttributeValue("itemprop", "recipeInstructions").first();
        for (Element s : stepContainer.getElementsByClass("step")) {
            Step step = new Step();
            step.setText(s.text());

            Element fancybox = s.getElementsByClass("fancybox").first();
            if (fancybox != null) {
                step.setImageUrl(fancybox.absUrl("href")
                        .replace("https://gotovim-doma.ru/images/recipe/", "")
                        .replace(".", "_m."));
            }
            step.setRecipe(recipe);
            recipe.getSteps().add(step);
        }

        // main image url retrieving
        Element photo = document.getElementsByClass("main-photo").first();
        String photo_url = photo.child(0).absUrl("href")
                .replace("https://gotovim-doma.ru/images/recipe/", "")
                .replace(".", "_m.");
        recipe.setMain_image_url(photo_url);
        return recipe;
    }

    @Override
    public List<Recipe> scrapeByCategory(int category_id) throws IOException {
        String startUrl = categoryRepository.findById(category_id).getCategory_url();
        int lastPage = getNumberOfPagesToScrapeFromThis(startUrl);
        Document document;
        List<Recipe> recipeList = new ArrayList<>();
        for (int i = 1; i <= lastPage; i++) {
            document = getDocument(startUrl + "?page=" + i);
            recipeList.addAll(scrapePage(document));
        }
        return recipeList;
    }

    private Integer getNumberOfPagesToScrapeFromThis(String startFrom) throws IOException {
        Document document = getDocument(startFrom);
        int lastPage = 1;

        try {
            lastPage = Integer.parseInt(document.getElementsByClass("pager").
                    first().getElementsByClass("last").first().text());
        } catch (NullPointerException npe) {
            System.out.println("There is just one page in category");
        }
        return lastPage;
    }

    private List<Recipe> scrapePage(Document document) {
        Elements elements = document.getElementsByClass("recept-item-img");
        List<String> urls = new ArrayList<>();
        for (Element e : elements) {
            urls.add(e.select("a[href]").attr("abs:href"));
        }
        List<Recipe> recipes = new ArrayList<>();
        for (String u : urls) {
            try {
                Recipe recipe = scrapeSingleRecipe(u);
                recipes.add(recipe);
            } catch (IOException ioe) {
                LOG.error(ioe.getMessage());
                ioe.printStackTrace();
            }
        }
        return recipes;
    }
}