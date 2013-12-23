package sk.gymdb.thinis.gcm.substitutions;

/**
 * Created by matejkobza on 21.12.2013.
 */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parse javascript from curl at http://gymdb.edupage.org/substitution/? pulls
 * out JSON objcts for substitutions
 */
public class SubstitutionParser {

    private static final String action = "switch";
    private static final String date = "2013-12-18";
    private static final String gpid = "3454974";
    private static final String gsh = "656a7f3b";
    private static final String __utmv = "182002547.edupage9; path=/";
    private static final String __utma = "182002547.1637320360.1387631395.1387631395.1387644012.2; path=/";
    private static final String __utmz = "182002547.1387631395.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); path=/";
    private static final String PHPSESSID = "rtrcmkn2p9lbpcjgui0hmmusd2; path=/";
    private static final String __utmb = "182002547.2.10.1387644012; path=/";
    private static final String __utmc = "182002547; path=/";

    public void getData() throws IOException {

        DefaultHttpClient httpClient = new DefaultHttpClient();

        // first load phpsesssionid
        HttpGet httpGet = new HttpGet("http://gymdb.edupage.org");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        System.out.println("Login form get: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }
        System.out.println("Initial set of cookies:");
        List<Cookie> cookies = httpClient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }


        HttpPost httpPost = new HttpPost("http://gymdb.edupage.org/substitution/gcall");
        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        nvps.add(new BasicNameValuePair("IDToken1", "username"));
        nvps.add(new BasicNameValuePair("IDToken2", "password"));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));


        String response1 = httpClient.execute(httpPost, responseHandler);

        System.out.println(response1);
//        entity = response1.getEntity();

//        System.out.println("Login form get: " + response.getStatusLine());
//        if (entity != null) {
//            entity.consumeContent();
//        }
//
//        System.out.println("Post logon cookies:");
//        cookies = httpClient.getCookieStore().getCookies();
//        if (cookies.isEmpty()) {
//            System.out.println("None");
//        } else {
//            for (int i = 0; i < cookies.size(); i++) {
//                System.out.println("- " + cookies.get(i).toString());
//            }
//        }

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpClient.getConnectionManager().shutdown();

//        HttpEntity entity = response.getEntity();
//
//        if (entity != null) {
//            entity.consumeContent();
//        }
//
//        System.out.println("Post logon cookies:");
//        List<Cookie> cookies = httpClient.getCookieStore().getCookies();
//        if (cookies.isEmpty()) {
//            System.out.println("None");
//        } else {
//            for (int i = 0; i < cookies.size(); i++) {
//                System.out.println("- " + cookies.get(i).toString());
//            }
//        }

//        entity.writeTo();

//        InputStreamReader rd = new InputStreamReader(entity.getContent()));
//        String line;
//        StringBuffer response1 = new StringBuffer();
//        while ((line = rd.readLine()) != null) {
//            response1.append(line);
//            response1.append("\r");
//        }
//        rd.close();
//
//        System.out.println("Output:" + response1.toString());
//
//        httpClient.getConnectionManager().shutdown();
//

//        URL url;
//        HttpURLConnection connection = null;
//        try {
//            Create connection
//            String urlParameters
//                    = "action=" + URLEncoder.encode(action, "UTF-8")
//                    + "&date=" + URLEncoder.encode(date, "UTF-8")
//                    + "&gpid=" + URLEncoder.encode(gpid, "UTF-8")
//                    + "&gsh=" + URLEncoder.encode(gsh, "UTF-8");
//
//            String cookies = "PHPSESSID=q4o2duq9c3dvtv4aes2c8kdrj1";
//
//            url = new URL("http://gymdb.edupage.org/substitution/gcall");
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type",
//                    "application/x-www-form-urlencoded");
//
//            connection.addRequestProperty("Content-Length", ""
//                    + Integer.toString(urlParameters.getBytes().length));
//            connection.addRequestProperty("Content-Language", "en-US");
//
//            do cookies
//            connection.addRequestProperty("Cookie", cookies);
//
//            connection.setUseCaches(false);
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//
//            connection.connect();
//
        //Send request
//            OutputStreamWriter writer = new OutputStreamWriter(
//                    connection.getOutputStream());
//            writer.writeBytes(urlParameters);
//            writer.flush();
//            writer.close();


        //Get Response
//            InputStream is = connection.getInputStream();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//            String line;
//            StringBuffer response = new StringBuffer();
//            while ((line = rd.readLine()) != null) {
//                response.append(line);
//                response.append("\r");
//            }
//            rd.close();
//
//            System.out.println("Output:" + response.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (connection != null) {
//                connection.disconnect();
//            }
//        }
    }
}