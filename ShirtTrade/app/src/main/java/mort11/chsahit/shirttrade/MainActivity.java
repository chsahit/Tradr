package mort11.chsahit.shirttrade;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //get field values
                String teamOwned = ((EditText) findViewById(R.id.editText)).getText().toString();
                String teamDesired = ((EditText) findViewById(R.id.editText2)).getText().toString();
                String name = ((EditText) findViewById(R.id.editText3)).getText().toString();
                String contactInfo = ((EditText) findViewById(R.id.editText4)).getText().toString();
                String offerInfo = getOffers(teamOwned, teamDesired);
                //pause to deal w/network things
                try {
                    Thread.sleep(1500, 0);
                } catch (InterruptedException e) {
                }
                TextView textView = (TextView) findViewById(R.id.editText5);
                //Log.d("tst",offerInfo);
                //if there is no trade, add the offer to the list. if there is, show the contact info
                if (offerInfo.equals("not found")) {
                    Log.d("tst", "if not found");
                    pushRequest(teamOwned, teamDesired, name, contactInfo);
                    textView.setText("Request Sent");
                } else {
                    textView.setText(offerInfo);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * search for matching offers
     * @param teamOwned the brand of the tshirt owned by the user
     * @param teamDesired the brand the user wants
     * @return the contac info of the matching trade
     */
    private String getOffers(String teamOwned, String teamDesired) {
        try{
            HttpURLConnection urlConnection = (HttpURLConnection)
                    new URL("http://289486ab.ngrok.io/checklist"+
                            "?shirt="+teamOwned+";"+teamDesired).openConnection();
            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line = "";
            String response = "";
            while ((line = rd.readLine()) != null){
                response += line;
            }
            rd.close();
            is.close();
            return response;
        } catch(Exception e){}
        return null;
    }
    //push the request for a shirt to the server
    private void pushRequest(String teamOwned, String teamDesired,
                             String name, String contactInfo) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://289486ab.ngrok.io/requestshirts");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("shirts", teamOwned + ";" + teamDesired));
            nameValuePairs.add(new BasicNameValuePair("contact", name + " " + contactInfo));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
        } catch (Exception e) {
        }
    }
}
