package sk.gymdb.thinis.gcm.login;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 14.1.2014
 * Time: 21:55
 */
public class LoginService {

    public String doLogin() throws IOException {
        String userName = "AdamLaurencik";
        String password = "970520/4960";

        WebClient client = new WebClient();
        HtmlPage page = client.getPage("https://gymdb.edupage.org/login/?msg=3");

        HtmlElement button = (HtmlElement) page.createElement("button");
        button.setAttribute("type", "submit");

// append the button to the form
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

// submit the form
        page = button.click();

        return page.asText();
    }

}
