package sk.gymdb.gymdbis;

import android.app.Activity;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

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

public class MainActivity extends Activity {
       Gymdb gymdb= new Gymdb();
       Button button;
       Button button2;
       int i=0;
        float x1=0;
        float x2=0;
        float y1=0;
        float y2=0;
        Context context;
        String msg="";
        GoogleCloudMessaging gcm;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.newsText);
        button2=(Button)findViewById(R.id.substitutionText);
        View newsView= (View) findViewById(R.id.newsText);
        final Button touchView= (Button) findViewById(R.id.substitutionText);
        DataDownloader downloader= new DataDownloader();
         gcm = GoogleCloudMessaging.getInstance(context);

        if(checkPlayServices()){
        Register register= new Register();
             register.execute();
        }
        downloader.execute("http://www.gymdb.sk/aktuality.html?page_id=118");
        newsView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gymdb.getNews()!=null){
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
                        if(gymdb.getNews()!=null){
                        if ((xdistance<80)){
                                if(ydistance<80){
                                }
                        }else
                        if(x1>x2){
                            if(i<gymdb.getNews().size()-1){
                                i=i+1;
                            }
                            if(gymdb.getNoticeById(i).getTitle().length()>80){
                                String title=gymdb.getNoticeById(i).getTitle().substring(0,77)+"...";
                                button.setText(Html.fromHtml("<b>" + title + "</b>"));
                            }else
                                button.setText(gymdb.getNoticeById(i).getHtmlString());
                        }else {
                            if(i>0){
                                i=i-1;
                            }
                            if(gymdb.getNoticeById(i).getTitle().length()>80){
                                String title=gymdb.getNoticeById(i).getTitle().substring(0,77)+"...";
                                button.setText(Html.fromHtml("<b>" + title + "</b>"));
                            }else
                                button.setText(gymdb.getNoticeById(i).getHtmlString());

                        }}

                        break;
                }}

                return true;
            }
        });

        }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS ) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("tag","This device is not supported.");
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
    public class Register extends AsyncTask<Void, String, String>{
        @Override
        protected String doInBackground(Void...voids) {
            while (msg=="SERVICE_NOT_AVAILABLE" || msg=="") {
            String regID;
                try {
                   regID=gcm.register("730751150432");
                    msg="connected"+regID;
                } catch (IOException e) {
                    msg=e.getMessage();
                    e.printStackTrace();
                }}
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            button2.setText(s);
        }
    }
    public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Explicitly specify that GcmIntentService will handle the intent.
            ComponentName comp = new ComponentName(context.getPackageName(),
                    MainActivity.class.getName());
            // Start the service, keeping the device awake while it is launching.
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);
        }
    }
    public class DataDownloader extends AsyncTask<String,Void,LinkedHashSet<Notice>> {

        @Override
        protected LinkedHashSet<Notice> doInBackground(String... strings) {
            publishProgress();
            if (isOnline()){
             URL url;
             LinkedHashSet<Notice> set= new LinkedHashSet<Notice>();
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
                    set.add(notice);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return set;}
            else
                return null;
        }

        @Override
        protected void onPostExecute(LinkedHashSet<Notice> notices) {
            if (notices!=null){
           gymdb.setNews(notices);
            if(gymdb.getNoticeById(i).getTitle().length()>80){
                String title=gymdb.getNoticeById(i).getTitle().substring(0,77)+"...";
                button.setText(Html.fromHtml("<b>" + title + "</b>"));
            }else
                button.setText(gymdb.getNoticeById(i).getHtmlString());

        } else button.setText("Chyba pripojenia");
            }

        @Override
        protected void onProgressUpdate(Void... values) {
            button.setText("Downloading data...");
        }
        public boolean isOnline(){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        }
    }



}
