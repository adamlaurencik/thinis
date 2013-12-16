package sk.gymdb.thinis;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sk.gymdb.thinis.model.dao.NewsDAO;

public class NewsActivity extends Activity {

    int i;
    WebView webView;
    NewsDAO newsDAO = NewsDAO.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_layout);
        webView = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        i = intent.getIntExtra("MESSAGE_ID", 1);
        final GetMessage getter = new GetMessage();
        getter.execute(i);
    }

    public class GetMessage extends AsyncTask<Integer, String, String> {


        @Override
        protected String doInBackground(Integer... params) {
            URL url;
            String result;
            String message = "";
            try {
                url = new URL(newsDAO.getNewsItemById(i).getUrl());

                URLConnection spoof = url.openConnection();
                spoof.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
                BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
                String strLine;
                String webPage = "";
                while ((strLine = in.readLine()) != null) {
                    webPage = webPage + strLine + "\n";
                }
                String html = webPage;
                Document doc = Jsoup.parse(html);
                message = doc.select("div[class=p10h]").toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            message = message.replaceAll("[^\\S ]+", "");
            message = message.replaceAll("<ul class=\"breadcrumbs\">(.+)</ul>", "");
            message = message.replaceAll("<div class=\"print_link clear\">(.+)</div>", "");
            Pattern pattern = Pattern.compile("(<div class=\"p10h\">)(.+)</div>");

            Matcher matcher = pattern.matcher(message);
            result = "";
            while (matcher.find()) {
                result = result + matcher.group();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            webView.loadDataWithBaseURL("", s, "text/html", "UTF-8", "");
        }
    }
}
