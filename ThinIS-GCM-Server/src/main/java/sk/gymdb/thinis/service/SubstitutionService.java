package sk.gymdb.thinis.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sk.gymdb.thinis.model.Substitution;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by matejkobza on 4.1.2014.
 */
public class SubstitutionService {

    private static final String SUBSTITUTION_URL = "http://gymdb.edupage.org/substitution/";

    private Set<Substitution> oldSubstitutions = new HashSet<Substitution>();

    public HtmlPage getSubstitutionsPage() throws IOException {
        WebClient webClient = new WebClient();
        return webClient.getPage(SUBSTITUTION_URL);
    }

    public Set<Substitution> parse() throws IOException {
        HtmlPage page = this.getSubstitutionsPage();
        // anchors for days
        List<HtmlAnchor> anchors = page.getAnchors();
        // set the correct parsing date (tommorow is our goal)
        DateFormat dateFormat = new SimpleDateFormat("d. M.");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1); // tommorow
        if (cal.get(Calendar.DAY_OF_WEEK) == 6) { // friday we need 2 more days
            cal.add(Calendar.DAY_OF_YEAR, 2);
        } else if (cal.get(Calendar.DAY_OF_WEEK) == 7) { // saturday we still need one more
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        Date tomorrowDate = new Date(cal.getTimeInMillis());
        String tomorrowDateString = dateFormat.format(tomorrowDate);

        // loop through anchors to get the right one
        for (HtmlAnchor htmlAnchor : anchors) {
            String anchorText = htmlAnchor.asText();
            if (anchorText.contains(tomorrowDateString)) {
                htmlAnchor.click();
            }
        }

        // load table with substitutions
        HtmlDivision tableDiv = (HtmlDivision) page.getByXPath("//div[@classname='yui-dt']").get(2);
        String htmlTable = tableDiv.asXml();
        int a=0;
        // here is some problem
        Document doc = Jsoup.parse(htmlTable);
        // here is some problem
        Element table = doc.select("tbody").get(1);
        Elements tableRows = table.select("tr");

        HashSet<Substitution> substitutions = new HashSet<Substitution>();

        for (Element tableRow : tableRows) {
            String subject = tableRow.select("td").get(2).text();
            // if its a dozor like row we dont care
            if (!subject.trim().equals("Dozor")) {
                Substitution sub = new Substitution();
                Element whoTd = tableRow.select("td").first();
                if (whoTd.select("span[style]").size() > 0) {
                    sub.setOdpada(Boolean.TRUE);
                } else {
                    sub.setOdpada(Boolean.FALSE);
                }

                String whoString = tableRow.select("td").first().text();
                String hour = tableRow.select("td").get(1).text();
                String teacher = tableRow.select("td").get(3).text();
                String comment = tableRow.select("td").get(4).text();

                List<String> whoArray = Arrays.asList(whoString.split("\\s*,\\s*"));
                sub.setWho(whoArray);
                sub.setComment(comment);
                sub.setHour(hour);
                sub.setTeacher(teacher);
                sub.setSubject(subject);

                substitutions.add(sub);
            }
        }
        return substitutions;
    }

    public HashMap<String, String> findSubstitutions() throws IOException {
        Set<Substitution> substitutions = this.parse();
        // calculate only new substitutions
        Set<Substitution> newSubstitutions = new HashSet<Substitution>();
        newSubstitutions.addAll(substitutions);
        newSubstitutions.removeAll(oldSubstitutions);

        HashMap<String, String> subs = new HashMap<String, String>();
        for (Substitution s : newSubstitutions) {
            for (String clazz : s.getWho()) {
                String msg = "Suplovačka: " + s.getHour() + " hodina, " + s.getComment();
                subs.put(clazz, msg);
            }
        }
        oldSubstitutions = substitutions;
        return subs;
    }
}

