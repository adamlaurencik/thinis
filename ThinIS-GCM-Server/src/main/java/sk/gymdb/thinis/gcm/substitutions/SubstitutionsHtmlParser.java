package sk.gymdb.thinis.gcm.substitutions;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

/**
 * Created by matejkobza on 4.1.2014.
 */
public class SubstitutionsHtmlParser {

    public SubstitutionsHtmlParser() {

    }

    public String parse() throws IOException {
        WebClient webClient = new WebClient();
//        webClient.setsetJavaScriptEnabled(false);
        // Get the first page
        HtmlPage page1 = webClient.getPage("http://gymdb.edupage.org/substitution/");
        String pageAsText = page1.asText();
        return pageAsText;
    }

}

