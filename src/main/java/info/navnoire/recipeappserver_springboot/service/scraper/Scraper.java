package info.navnoire.recipeappserver_springboot.service.scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Victoria Berezina on 14/05/2021 in RecipeAppServer_SpringBoot project
 */
public interface Scraper {
    Logger LOG = LoggerFactory.getLogger(Scraper.class);

    default Document getDocument(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        Document document;
        document = connection.get();
        return document;
    }

    default Integer extractId(String string) {
        string = string.replace("https://gotovim-doma.ru/", "");
        int index = string.indexOf('-');
        if (index != -1) string = string.substring(0, index);
        try {
            return Integer.parseInt(string.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }
}
