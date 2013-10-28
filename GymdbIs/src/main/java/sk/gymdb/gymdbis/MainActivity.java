package sk.gymdb.gymdbis;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashSet;

public class MainActivity extends ActionBarActivity {
       Gymdb gymdb= new Gymdb();
       Button button;
       int i=0;
        float x1=0;
        float x2=0;
        float y1=0;
        float y2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.newsText);
        View newsView= (View) findViewById(R.id.newsText);
        final Button touchView= (Button) findViewById(R.id.substitutionText);
        DataDownloader downloader= new DataDownloader();
        downloader.execute("http://www.gymdb.sk/aktuality.html?page_id=118");
        newsView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action= motionEvent.getAction();


                switch (action){
                    case MotionEvent.ACTION_DOWN:
                          x1=motionEvent.getX();
                          y1=motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                         x2=motionEvent.getX();
                         y2=motionEvent.getY();
                         float xdistance=Math.abs(x2-x1);
                         float ydistance=Math.abs(y2-y1);
                        if (ydistance<80){
                        if ((xdistance<80)){
                            //kliknutie
                        }else
                        if(x1>x2){
                            if(i>0){
                                i=i-1;
                            }
                            else{
                                i=gymdb.getNews().size()-1;
                            }
                            if(gymdb.getNoticeById(i).getTitle().length()>80){
                                String title=gymdb.getNoticeById(i).getTitle().substring(0,77)+"...";
                                button.setText(Html.fromHtml("<b>" + title + "</b>"));
                            }else
                                button.setText(gymdb.getNoticeById(i).getHtmlString());
                        }else {
                            if(i<gymdb.getNews().size()-1){
                                i=i+1;
                            }
                            else{
                                i=0;
                            }
                            if(gymdb.getNoticeById(i).getTitle().length()>80){
                                String title=gymdb.getNoticeById(i).getTitle().substring(0,77)+"...";
                                button.setText(Html.fromHtml("<b>" + title + "</b>"));
                            }else
                                button.setText(gymdb.getNoticeById(i).getHtmlString());

                        }

                        break;
                }}

                return true;
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
    public class DataDownloader extends AsyncTask<String,Void,LinkedHashSet<Notice>> {

        @Override
        protected LinkedHashSet<Notice> doInBackground(String... strings) {
            publishProgress();
            URL url = null;
            Gymdb help= new Gymdb();
            try{

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
                    Notice notice=new Notice();
                    Element announcement=doc.select("div[class=articlebox]").get(i);
                    notice.setTitle(announcement.select("h2").text());
                    notice.setMessage(announcement.select("div[class=gray]").text());
                    help.addNotice(notice);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return help.getNews();
        }

        @Override
        protected void onPostExecute(LinkedHashSet<Notice> notices) {
           gymdb.setNews(notices);
            if(gymdb.getNoticeById(i).getTitle().length()>80){
                String title=gymdb.getNoticeById(i).getTitle().substring(0,77)+"...";
                button.setText(Html.fromHtml("<b>" + title + "</b>"));
            }else
                button.setText(gymdb.getNoticeById(i).getHtmlString());

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            button.setText("Downloading data...");
        }
    }



}
