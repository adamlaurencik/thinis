package sk.gymdb.thinis.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
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
        // load table with substitutions
        Calendar cal = Calendar.getInstance();
        Date tomorrowDate = new Date(cal.getTimeInMillis());
        HtmlDivision tableDiv = (HtmlDivision) page.getByXPath("//div[@classname='yui-dt']").get(2);
        String htmlTable = tableDiv.asXml();
        Document doc = Jsoup.parse(htmlTable);
        Element table = doc.select("tbody").get(1);
        Elements tableRows = table.select("tr");

        HashSet<Substitution> substitutions = new HashSet<Substitution>();

        for (Element tableRow : tableRows) {
            String subjectString = tableRow.select("td").get(2).text();
            // if its a dozor like row we dont care
            if (!subjectString.trim().equals("Dozor")) {
                Substitution sub = new Substitution();
                Element whoTd = tableRow.select("td").first();
                if (whoTd.select("span[style]").size() > 0) {
                    sub.setOdpada(Boolean.TRUE);
                } else {
                    sub.setOdpada(Boolean.FALSE);
                }
                
                if(tableRow.select("td").get(2).select("span").size()==2){
                    subjectString = tableRow.select("td").get(2).select("span").get(1).attr("title");
                    sub.setSubjectChange(Boolean.TRUE);
                } else {
                    subjectString = tableRow.select("td").get(2).select("span").first().attr("title");
                    sub.setSubjectChange(Boolean.FALSE);
                }
                String whoString = tableRow.select("td").first().text();
                String hour = tableRow.select("td").get(1).text();
                String teacherString = tableRow.select("td").get(3).text();
                String comment = tableRow.select("td").get(5).text();
                String clazzString= tableRow.select("td").get(4).text();//
                                
                 if(teacherString.contains("➔")){
                 sub.setTeacherChange(Boolean.TRUE);
                 sub.setTeacher(teacherString.substring(teacherString.indexOf("➔")+1,teacherString.length() ));
                }else{
                 sub.setTeacherChange(Boolean.FALSE);
                 sub.setTeacher(teacherString);
                }
                
                if(clazzString.contains("➔")){
                 sub.setClazzChange(Boolean.TRUE);
                 sub.setClazz(clazzString.substring(clazzString.indexOf("➔")+1, clazzString.length() ));
                }else{
                 sub.setClazzChange(Boolean.FALSE);
                 sub.setClazz(clazzString);
                }
                
                List<String> whoArray = Arrays.asList(whoString.split("\\s*,\\s*"));
                sub.setWho(whoArray);
                sub.setDate(tomorrowDate);
                sub.setComment(comment);
                sub.setHour(hour);
                int id= (int) new Date().getTime();
                sub.setID(id);
                sub.setSubject(subjectString);
                substitutions.add(sub);
                System.out.println(sub.toString()+" "+sub.getWho()); 
            }
        }
        List<HtmlAnchor> anchors = page.getAnchors();
        // set the correct parsing date (tommorow is our goal)
        DateFormat dateFormat = new SimpleDateFormat("d. M.");
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1); // tommorow
        if (cal.get(Calendar.DAY_OF_WEEK) == 7) { // friday we need 2 more days
            cal.add(Calendar.DAY_OF_YEAR, 2);
        } else if (cal.get(Calendar.DAY_OF_WEEK) == 1) { // saturday we still need one more
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        tomorrowDate = new Date(cal.getTimeInMillis());
        String tomorrowDateString = dateFormat.format(tomorrowDate);

        // loop through anchors to get the right one
        for (HtmlAnchor htmlAnchor : anchors) {
            String anchorText = htmlAnchor.asText();
            if (anchorText.contains(tomorrowDateString)) {
                page=htmlAnchor.click();
            }
        }

        // load table with substitutions
         tableDiv = (HtmlDivision) page.getByXPath("//div[@classname='yui-dt']").get(2);
         htmlTable = tableDiv.asXml();
         doc = Jsoup.parse(htmlTable);
         table = doc.select("tbody").get(1);
         tableRows = table.select("tr");
        for (Element tableRow : tableRows) {
            String subjectString = tableRow.select("td").get(2).text();
            // if its a dozor like row we dont care
            if (!subjectString.trim().equals("Dozor")) {
                Substitution sub = new Substitution();
                Element whoTd = tableRow.select("td").first();
                if (whoTd.select("span[style]").size() > 0) {
                    sub.setOdpada(Boolean.TRUE);
                } else {
                    sub.setOdpada(Boolean.FALSE);
                }
                
                if(tableRow.select("td").get(2).select("span").size()==2){
                    subjectString = tableRow.select("td").get(2).select("span").get(1).attr("title");
                    sub.setSubjectChange(Boolean.TRUE);
                } else {
                    subjectString = tableRow.select("td").get(2).select("span").first().attr("title");
                    sub.setSubjectChange(Boolean.FALSE);
                }
                String whoString = tableRow.select("td").first().text();
                String hour = tableRow.select("td").get(1).text();
                String teacherString = tableRow.select("td").get(3).text();
                String comment = tableRow.select("td").get(5).text();
                String clazzString= tableRow.select("td").get(4).text();//
                                
                 if(teacherString.contains("➔")){
                 sub.setTeacherChange(Boolean.TRUE);
                 sub.setTeacher(teacherString.substring(teacherString.indexOf("➔")+1,teacherString.length() ));
                }else{
                 sub.setTeacherChange(Boolean.FALSE);
                 sub.setTeacher(teacherString);
                }
                
                if(clazzString.contains("➔")){
                 sub.setClazzChange(Boolean.TRUE);
                 sub.setClazz(clazzString.substring(clazzString.indexOf("➔")+1, clazzString.length() ));
                }else{
                 sub.setClazzChange(Boolean.FALSE);
                 sub.setClazz(clazzString);
                }
                
                List<String> whoArray = Arrays.asList(whoString.split("\\s*,\\s*"));
                sub.setWho(whoArray);
                sub.setDate(tomorrowDate);
                sub.setComment(comment);
                sub.setHour(hour);
                int id= (int) new Date().getTime();
                sub.setID(id);
                sub.setSubject(subjectString);
                substitutions.add(sub);
                System.out.println(sub.toString()+" "+sub.getWho()); 
            }
        }
        return substitutions;
    }

    public Set<Substitution> findSubstitutions() throws IOException {
        Set<Substitution> substitutions = this.parse();
        // calculate only new substitutions
        Set<Substitution> newSubstitutions = new HashSet<Substitution>();
        newSubstitutions.addAll(substitutions);
        newSubstitutions.removeAll(oldSubstitutions);
        System.out.println(substitutions+"\n ------------------------------------------------");
        System.out.println(newSubstitutions);
        oldSubstitutions = substitutions;
        return newSubstitutions;
    }
}

