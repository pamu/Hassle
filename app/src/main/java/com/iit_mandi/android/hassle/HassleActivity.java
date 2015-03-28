package com.iit_mandi.android.hassle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.net.SocketTimeoutException;
import java.net.URL;


public class HassleActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hassle);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hassle, menu);
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        final String LOG_TAG = PlaceholderFragment.class.getSimpleName();

        private ListAdapter dayInfoAdapter;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_hassle, container, false);
            TextView heading = (TextView) rootView.findViewById(R.id.heading);
            GridView dayInfoGrid = (GridView) rootView.findViewById(R.id.grid_day_info);

            new DayInfoLoader(getActivity()).execute(new Integer[]{0});

            return rootView;
        }

        public class DayInfoLoader extends AsyncTask<Integer, Void, String> {
            final String URL = "http://bighassle.herokuapp.com/day";
            private Activity activity;
            private ProgressDialog dialog;

            public DayInfoLoader(Activity activity) {
                this.activity = activity;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(activity);
                dialog.setIndeterminate(true);
                dialog.setTitle("Processing ...");
                dialog.setMessage("Getting today\'s schedule from cloud.");
                dialog.show();
            }

            @Override
            protected String doInBackground(Integer... params) {
                Uri dayInfoUri = Uri.parse(URL).buildUpon().
                        appendQueryParameter("day", params[0].toString()).build();
                String jsonStr = null;
                try {
                    URL url = new URL(dayInfoUri.toString());
                    Log.d(LOG_TAG, url.toString());

                    HttpGet get = new HttpGet(url.toString());

                    HttpParams httpParams = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParams, 10000);

                    HttpClient client = new DefaultHttpClient(httpParams);

                    HttpResponse response = client.execute(get);

                    if(response == null) return null;
                    HttpEntity entity = response.getEntity();
                    if(entity == null) return null;
                    if(response.getStatusLine().getStatusCode() == 200) {
                        jsonStr = EntityUtils.toString(entity);
                        Log.i(LOG_TAG, jsonStr);
                        Log.i(LOG_TAG, "Got data successfully from BigHassle");
                    }
                }
                catch (ConnectTimeoutException cte) {
                    cte.printStackTrace();
                    Log.i(LOG_TAG, "Connection timed out");
                }
                catch (SocketTimeoutException ste) {
                    Log.i(LOG_TAG, "Socket timed out");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    Log.i(LOG_TAG, "Fetching data failed");
                    Log.d(LOG_TAG, ex.getMessage());
                }
                if (jsonStr == null) return null;
                return jsonStr;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                if (s != null)
                    Log.d(LOG_TAG, s);
                else Log.d(LOG_TAG, "string is null");
            }
        }
    }
}
