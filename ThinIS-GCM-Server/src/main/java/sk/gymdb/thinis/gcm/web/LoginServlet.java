package sk.gymdb.thinis.gcm.web;

import sk.gymdb.thinis.gcm.login.LoginService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 14.1.2014
 * Time: 22:06
 */
public class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        LoginService loginService = new LoginService();
        try {
            PrintWriter writer = resp.getWriter();

            writer.append(loginService.doLogin());

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
