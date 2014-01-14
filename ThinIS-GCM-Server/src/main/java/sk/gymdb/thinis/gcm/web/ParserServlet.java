package sk.gymdb.thinis.gcm.web;

import sk.gymdb.thinis.gcm.substitutions.SubstitutionsHtmlParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 14.1.2014
 * Time: 19:46
 */
public class ParserServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        SubstitutionsHtmlParser parser = new SubstitutionsHtmlParser();
        out.print(parser.parse());

        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doGet(req, resp);
    }
}
