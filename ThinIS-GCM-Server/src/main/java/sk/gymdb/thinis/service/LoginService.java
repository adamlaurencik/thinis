package sk.gymdb.thinis.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;
import sk.gymdb.thinis.model.UserInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 14.1.2014
 * Time: 21:55
 */
public class LoginService {

    HtmlPage page;

    /**
     * Returns true if login successfull false otherwise
     *
     * @param userName
     * @param password
     * @return
     * @throws IOException
     */
    public boolean doLogin(String userName, String password) throws IOException {
//        String userName = "AdamLaurencik";
//        String password = "970520/4960";

        WebClient client = new WebClient();
        page = client.getPage("https://gymdb.edupage.org/login/?msg=3");

        HtmlElement button = (HtmlElement) page.createElement("button");
        button.setAttribute("type", "submit");

        HtmlElement form = (HtmlElement) page.createElement("form");
        form.setAttribute("action", "https://gymdb.edupage.org/login/edubarLogin.php");
        form.setAttribute("enctype", "multipart/form-data");
        form.setAttribute("method", "post");

        HtmlElement username = (HtmlElement) page.createElement("input");
        username.setAttribute("type", "text");
        username.setAttribute("name", "username");
        username.setAttribute("value", userName);

        HtmlElement pass = (HtmlElement) page.createElement("input");
        pass.setAttribute("type", "password");
        pass.setAttribute("value", password);
        pass.setAttribute("name", "password");

        form.appendChild(username);
        form.appendChild(pass);
        form.appendChild(button);

        page = button.click();
        page = client.getPage("https://gymdb.edupage.org/znamky/?");

        String htmlCode = page.asText();

        if (htmlCode.contains("Ste prihlásený ako ")) {
            return true;
        } else {
            return true;
        }
    }

    public UserInfo getUserInfo() {
        String pageAsText = page.asText();
        String htmlCode = page.asXml();
        UserInfo userInfo = new UserInfo();

        // user name
        int index = pageAsText.indexOf("Ste prihlásený ako ");
        String name = pageAsText.substring(index + 19);
        name = name.substring(0, StringUtils.ordinalIndexOf(name, "\n", 1));
        userInfo.setName(name);

        // grades
        HtmlElement element = (HtmlElement) page.getByXPath("//table[contains(@class, 'edubarTable')]").get(0);
        List<HtmlElement> rows = element.getElementsByAttribute("tr", "style", "cursor:pointer");

        for (int i = 0; i < rows.size(); i++) {
            HtmlElement row = rows.get(i);
            Iterable<DomElement> childrens = row.getChildElements();
            Iterator it = childrens.iterator();
            String subject = ((HtmlElement) it.next()).getTextContent();
            ArrayList<String> grades = new ArrayList<String>();
            while (it.hasNext()) {
                HtmlElement element1 = (HtmlElement) it.next();
                if (!element1.getTextContent().isEmpty()) {
                    grades.add(element1.getTextContent());
                }
                if (element1.hasAttribute("colspan")) {
                    break;
                }
            }
            userInfo.addEvaluation(subject, grades);
        }
        return userInfo;
    }

}
