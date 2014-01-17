package sk.gymdb.thinis.gcm.web;

import com.google.gson.Gson;
import sk.gymdb.thinis.gcm.login.LoginService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    /**
     * This should be replaced by post. Servlet should support only POST request not GET
     * @param req
     * @param resp
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("text/plain; charset=utf-8");
            resp.setCharacterEncoding("UTF-8");
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
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
