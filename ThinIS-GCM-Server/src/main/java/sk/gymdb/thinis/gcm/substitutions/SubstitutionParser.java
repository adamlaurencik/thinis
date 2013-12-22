package sk.gymdb.thinis.gcm.substitutions;

/**
 * Created by matejkobza on 21.12.2013.
 */

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Parse javascript from curl at http://gymdb.edupage.org/substitution/?
 * pulls out JSON objcts for substitutions
 */
public class SubstitutionParser {
    private static final String action = "switch";
    private static final String date= "2013-12-18";
    private static final String gpid="3454974";
    private static final String gsh="656a7f3b";
    private static final String __utmv="182002547.edupage9; path=/";
    private static final String __utma="182002547.1637320360.1387631395.1387631395.1387644012.2; path=/";
    private static final String __utmz="182002547.1387631395.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); path=/";
    private static final String PHPSESSID="rtrcmkn2p9lbpcjgui0hmmusd2; path=/";
    private static final String __utmb="182002547.2.10.1387644012; path=/";
    private static final String __utmc="182002547; path=/";
    public void GetData(){
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            String urlParameters =
        "action=" + URLEncoder.encode(action, "UTF-8") +
        "date=" + URLEncoder.encode(date, "UTF-8")+
        "gpid=" + URLEncoder.encode(gpid, "UTF-8")+
        "gsh=" + URLEncoder.encode(gsh, "UTF-8");
            url = new URL("http://gymdb.edupage.org/substitution/gcall");
            connection = (HttpURLConnection)url.openConnection(); 
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();
    } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
