package sk.gymdb.thinis.gcm.substitutions;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by matejkobza on 4.1.2014.
 */
public class SubstitutionsHtmlParser {

    public SubstitutionsHtmlParser() {

    }

    @SuppressWarnings("empty-statement")
    public String parse() throws IOException {
        WebClient webClient = new WebClient();
//        webClient.setsetJavaScriptEnabled(false);
        // Get the first page
        HtmlPage page1 = webClient.getPage("http://gymdb.edupage.org/substitution/");
        HtmlPage tomorrowPage = null;
        String pageAsText="";
        List<HtmlAnchor> anchors1 = page1.getAnchors();
        DateFormat dateFormat = new SimpleDateFormat("d. M.");
        Date todayDate = new Date();
        Date tomorrowDate = new Date(todayDate.getTime() + (1000 * 60 * 60 * 24));
        String currentDate=dateFormat.format(todayDate);
        String tomorrowDateString=dateFormat.format(tomorrowDate);
        for (HtmlAnchor htmlAnchor : anchors1) {
            String anchorText=htmlAnchor.asText();
           if (anchorText.contains(tomorrowDateString)){
                tomorrowPage = htmlAnchor.click();
            }
        }
        return tomorrowPage.asText();
    }

}

