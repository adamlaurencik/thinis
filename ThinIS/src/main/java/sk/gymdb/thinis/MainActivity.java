package sk.gymdb.thinis;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import sk.gymdb.thinis.gcm.GcmServiceException;
import sk.gymdb.thinis.gcm.service.GcmService;

/**
 * This is the Entry point for the application
 * @author matejkobza
 */
public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        boolean isProceedError = false;

        // if there is no connection just skip all the steps bellow and continue
        if (!isNetworkConnected()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage(getResources().getString(R.string.no_internet_connection));
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    proceed();
                }
            });
            dialogBuilder.show();

            // this might change in the future
            Log.e(TAG, "No internet connection");
            return;
//            proceed();
        }

        // check if is registered and if not, than register
        try {
            new GcmService(this);
        } catch (GcmServiceException ex) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.gcm_service);
            dialog.setMessage(getResources().getString(R.string.gcm_service_error));
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    proceed();
                }
            });
            dialog.show();
            Log.e(TAG, "Gcm services unavailable");
            return;
        }

        // check if the user has filled username and password, if not ask for username and password
        SharedPreferences prefs = this.getApplicationContext().getSharedPreferences("APPLICATION", Context.MODE_PRIVATE);
        if (prefs.getString("username", "").isEmpty() || prefs.getString("password", "").isEmpty()) {
            Log.i(TAG, "None credentials found");
            Intent activityChangeIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(activityChangeIntent);
        }

        // check if the user has filled his class
//        if (prefs.getString("clazz", "").isEmpty()) {
//            Log.i(TAG, "No class selected");
//            Intent activityChangeIntent = new Intent(MainActivity.this, SettingsActivity.class);
//            MainActivity.this.startActivity(activityChangeIntent);
//        }

        if (!isProceedError) {
            proceed();
        }

    }

    private void proceed() {
        Intent activityChangeIntent = new Intent(MainActivity.this, HomeActivity.class);

        // currentContext.startActivity(activityChangeIntent);

        MainActivity.this.startActivity(activityChangeIntent);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

//    NewsDAO newsService = NewsDAO.getInstance();
//    Button button;
//    int i = 0;
//    float X;
//    Context context = this;
//    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        button = (Button) findViewById(R.id.newsText);
//        View newsView = findViewById(R.id.newsText);
//        DataDownloader downloader = new DataDownloader();
//        GcmRegister gcmRegister = new GcmRegister();
//        downloader.execute("http://www.gymdb.sk");
//        gcmRegister.execute();
//        newsView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (newsService.getNews().size() > 0) {
//                    float distance;
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            X = event.getX();
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            distance = event.getX() - X;
//                            if (Math.abs(distance) > 30) {
//                                if (distance > 0) {
//                                    if (i > 0) i--;
//                                }
//                                if (distance < 0) {
//                                    if (i < newsService.getNews().size() - 1) i++;
//                                }
//
//                                if (newsService.getNewsItemById(i).getTitle().length() > 80) {
//                                    String title = newsService.getNewsItemById(i).getTitle().substring(0, 77) + "...";
//                                    button.setText(Html.fromHtml("<b>" + title + "</b>"));
//                                } else
//                                    button.setText(newsService.getNewsItemById(i).getHtmlString());
//                            } else {
//                                Intent intent = new Intent(context, NewsActivity.class);
//                                intent.putExtra("MESSAGE_ID", i);
//                                startActivity(intent);
//                            }
//                    }
//
//                }
//
//                return false;
//            }
//        });
//    }
//
//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        9000).show();
//            } else {
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public class GcmRegister extends AsyncTask<String, Void, String> {
//        String message;
//
//        protected String doInBackground(String... strings) {
//            String regId = "";
//            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPref.edit();
//            if (sharedPref.contains("REGID")) {
//                regId = sharedPref.getString("REGID", "");
//                message = "Already registered";
//            } else {
//                if (checkPlayServices()) {
//
//                    while (regId.equals("")) {
//                        try {
//                            regId = gcm.register("1045030114303");
//                            sendRegId(regId);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                editor.putString("REGID", regId);
//                editor.commit();
//                message = "Registered now";
//                Log.e("REGISTRATION", regId);
//
//            }
//            sendRegId(regId);
//            return regId;
//        }
//
//        protected void onPostExecute(String regId) {
//            Toast.makeText(context, message + ": " + regId, Toast.LENGTH_LONG).show();
//
//        }
//    }
//
//    private void sendRegId(String regId) {
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost("http://192.168.2.54:8084/ThinIS-GCM-Server/register");
//
//        try {
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//            nameValuePairs.add(new BasicNameValuePair("regId", regId));
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//            httpclient.execute(httppost);
//        }catch (Exception ex) {
//            Toast.makeText(context, R.string.regid_registration_unsuccessful, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public class DataDownloader extends AsyncTask<String, Void, ArrayList<NewsItem>> {
//
//        @Override
//        protected ArrayList<NewsItem> doInBackground(String... strings) {
//            publishProgress();
//            URL url;
//            ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
//
//            try {
//
//                url = new URL(strings[0]);
//                URLConnection spoof = url.openConnection();
//                spoof.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
//                BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
//                String strLine;
//                String webPage = "";
//
//                //Loop through every line in the source
//                while ((strLine = in.readLine()) != null) {
//                    webPage = webPage + strLine + "\n";
//                }
//                String html = webPage;
//                Document doc = Jsoup.parse(html);
//                Elements news = doc.select("div[class=articlebox]");
//                for (int i = 0; i < news.size(); i++) {
//                    NewsItem newsItem = new NewsItem();
//                    Element announcement = doc.select("div[class=articlebox]").get(i);
//                    newsItem.setTitle(announcement.select("h2").text());
//                    newsItem.setMessage(announcement.select("div[class=gray]").text());
//                    String urll = announcement.select("a").attr("href");
//                    newsItem.setUrl(urll);
//                    newsItems.add(newsItem);
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return newsItems;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<NewsItem> newsItems) {
//            newsService.setNews(newsItems);
//            if (newsService.getNewsItemById(i).getTitle().length() > 80) {
//                String title = newsService.getNewsItemById(i).getTitle().substring(0, 77) + "...";
//                button.setText(Html.fromHtml("<b>" + title + "</b>"));
//            } else
//                button.setText(newsService.getNewsItemById(i).getHtmlString());
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            button.setText("Downloading data...");
//        }
//    }
}
