package sk.gymdb.thinis.gcm.substitutions;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        HtmlDivision tableDiv= (HtmlDivision) page1.getByXPath("//div[@classname='yui-dt']").get(2);
        String htmlTable = tableDiv.asXml();
        Document doc= Jsoup.parse(htmlTable);
        Element table=doc.select("tbody").get(1);
        Elements substitutions = table.select("tr");
        HashSet<Substitution> set;
        set = new HashSet<Substitution>();
        for (Element substitution : substitutions) {
            Substitution sub=new Substitution();
            String who = substitution.select("td").first().text();
            String hour= substitution.select("td").get(2).text();
            String subject = substitution.select("td").get(3).text();
            String teacher= substitution.select("td").get(4).text();
            String comment= substitution.select("td").get(5).text();
            sub.setComment(comment);
            sub.setHour(hour);
            sub.setTeacher(teacher);
            sub.setSubject(subject);
            if(!subject.equals("Dozor")){
                set.add(sub);
            }
            
        }
        return set.toString();
        

    }

}

