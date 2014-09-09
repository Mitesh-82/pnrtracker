package com.droidsoft.pnrtracker.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.droidsoft.pnrtracker.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


public class PNRView extends Activity {

    private TextView pnrResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pnrview);
        pnrResponse = (TextView) findViewById(R.id.textView);

        RetrievePERStatus getPnrStatus = new RetrievePERStatus();

        getPnrStatus.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pnrview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class RetrievePERStatus extends AsyncTask<Void, Void, String> {
        String response;

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return getPnrResults();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }


        protected String getPnrResults() throws IOException {

            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://www.kirant400.com/irctc/pnr.php?pnrno=8726709666&rtype=JSON";
            String serverResponse = "";

            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            serverResponse = httpClient.execute(httpget, responseHandler);
            response = serverResponse;
            return serverResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            pnrResponse.setText(response);
        }
    }
}
