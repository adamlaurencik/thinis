package sk.gymdb.thinis.gcm.substitutions;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Created by matejkobza on 4.1.2014.
 */
public class SubstitutionsHtmlParser {

    public SubstitutionsHtmlParser() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = webClient.getPage("http://gymdb.edupage.org");

        page.getBy...
    }

}
