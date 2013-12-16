package sk.gymdb.thinis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;


import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import sk.gymdb.thinis.news.NewsItem;
import sk.gymdb.thinis.news.NewsService;


public class MainActivity extends Activity {
    NewsService newsService =NewsService.getInstance();
    Button button;
    int i = 0;
    float X;
    Context context=this;
    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.newsText);
        View newsView = (View) findViewById(R.id.newsText);
        DataDownloader downloader = new DataDownloader();
         GcmRegister gcmRegister= new GcmRegister();
        downloader.execute("http://www.gymdb.sk");
        gcmRegister.execute();
        newsView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(newsService.getNews().size()>0){
                    float distance=0;
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            X=event.getX();
                            break;
                        case MotionEvent.ACTION_UP:
                            distance=event.getX()-X;
                            if(Math.abs(distance)>30){
                                if (distance>0){
                                    if(i>0)i--;
                                }
                                if(distance<0){
                                    if(i<newsService.getNews().size()-1)i++;
                                }

                                if (newsService.getNewsItemById(i).getTitle().length() > 80) {
                                    String title = newsService.getNewsItemById(i).getTitle().substring(0, 77) + "...";
                                    button.setText(Html.fromHtml("<b>" + title + "</b>"));
                                } else
                                    button.setText(newsService.getNewsItemById(i).getHtmlString());
                            }else {
                                Intent intent=new Intent(context,NewsActivity.class);
                                intent.putExtra("MESSAGE_ID",i);
                                startActivity(intent);
                            }}

                }

                return false;
            }
        });
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        9000).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
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
    public class GcmRegister extends AsyncTask<String, Void, String>{
    String message;
    protected String doInBackground(String...strings){
        String regId="";
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(sharedPref.contains("REGID")){
            regId=sharedPref.getString("REGID","");
            message="Already registered";
        } else {
            if (checkPlayServices()){

                while (regId.equals("")){
                    try {
                        regId=gcm.register("1045030114303");
                        sendRegId(regId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            editor.putString("REGID",regId);
            editor.commit();
            message="Registered now";
            Log.e("REGISTRATION",regId);

        }
        sendRegId(regId);
        return regId;
    }
       protected void onPostExecute(String regId){
           Toast.makeText(context,message+": "+regId,Toast.LENGTH_LONG).show();

       }
    }

    private void sendRegId(String regId) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://192.168.2.54:8084/ThinIS-GCM-Server/register");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("regId", regId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpclient.execute(httppost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
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
                     String urll=announcement.select("a").attr("href");
                    newsItem.setUrl(urll);
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
