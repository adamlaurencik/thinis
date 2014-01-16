package sk.gymdb.thinis.gcm.web;

import com.google.gson.Gson;
import sk.gymdb.thinis.gcm.login.LoginService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 14.1.2014
 * Time: 22:06
 */
public class LoginServlet extends HttpServlet {

    private Gson gson;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LoginService loginService = new LoginService();
        gson = new Gson();
        try {
            PrintWriter writer = resp.getWriter();
            String username = req.getParameter("u");
            String password = req.getParameter("p");

            if (loginService.doLogin(username, password)) {
                writer.append(gson.toJson(loginService.getUserInfo()));
            }
            resp.setContentType("text/plain; charset=utf-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
