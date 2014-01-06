package sk.gymdb.thinis.gcm.substitutions;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import java.io.IOException;
import static java.lang.System.out;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by matejkobza on 4.1.2014.
 */
public class SubstitutionsHtmlParser {

    public SubstitutionsHtmlParser(){
        
    }
    public String parse() throws IOException{
        final WebClient webClient = new WebClient();
        //webClient.setJavaScriptEnabled(false);
        // Get the first page
        final HtmlPage page1 = webClient.getPage("http://gymdb.edupage.org/substitution/");
        final String pageAsText = page1.asXml();
        return pageAsText;
    }

  

}

