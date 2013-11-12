package sk.gymdb.gymdbis;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sk.gymdb.gymdbis.news.NewsItem;
import sk.gymdb.gymdbis.news.NewsService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashSet;

public class MainActivity extends ActionBarActivity {
    NewsService newsService = new NewsService();
    Button button;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.newsText);
        View newsView = (View) findViewById(R.id.newsText);

        DataDownloader downloader = new DataDownloader();
        downloader.execute("http://www.newsService.sk/aktuality.html?page_id=118");
        newsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i < newsService.getNews().size() - 1) {
                    i = i + 1;
                } else {
                    i = 0;
                }
                if (newsService.getNewsItemById(i).getTitle().length() > 80) {
                    String title = newsService.getNewsItemById(i).getTitle().substring(0, 77) + "...";
                    button.setText(Html.fromHtml("<b>" + title + "</b>"));
                } else
                    button.setText(newsService.getNewsItemById(i).getHtmlString());

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class DataDownloader extends AsyncTask<String, Void, LinkedHashSet<NewsItem>> {

        @Override
        protected LinkedHashSet<NewsItem> doInBackground(String... strings) {
            publishProgress();
            URL url = null;
            NewsService help = new NewsService();
            try {

                url = new URL(strings[0]);
                URLConnection spoof = url.openConnection();
                spoof.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
                BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
                String strLine = "";
                String webPage = "";

                //Loop through every line in the source
                while ((strLine = in.readLine()) != null) {
                    webPage = webPage + strLine + "\n";
                }
                String html = webPage;
                Document doc = Jsoup.parse(html);
                Elements news = doc.select("div[class=articlebox]");
                for (int i = 0; i < news.size(); i++) {
                    NewsItem newsItem = new NewsItem();
                    Element announcement = doc.select("div[class=articlebox]").get(i);
                    newsItem.setTitle(announcement.select("h2").text());
                    newsItem.setMessage(announcement.select("div[class=gray]").text());
                    help.addNewsItem(newsItem);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return help.getNews();
        }

        @Override
        protected void onPostExecute(LinkedHashSet<NewsItem> newsItems) {
            newsService.setNews(newsItems);
            if (newsService.getNewsItemById(i).getTitle().length() > 80) {
                String title = newsService.getNewsItemById(i).getTitle().substring(0, 77) + "...";
                button.setText(Html.fromHtml("<b>" + title + "</b>"));
            } else
                button.setText(newsService.getNewsItemById(i).getHtmlString());

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            button.setText("Downloading data...");
        }
    }


}
