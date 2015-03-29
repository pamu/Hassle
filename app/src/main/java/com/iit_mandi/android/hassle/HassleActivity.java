package com.iit_mandi.android.hassle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


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

        private ArrayAdapter<List<String>> dayInfoArrayAdapter;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_hassle, container, false);
            TextView heading = (TextView) rootView.findViewById(R.id.heading);
            ListView dayInfoListView = (ListView) rootView.findViewById(R.id.day_info_list);
            //dayInfoListView.addHeaderView(heading);
            heading.setText(new SimpleDateFormat("EEE, dd MM").format(new Date()) + " Menu.");

            String[] row = {"no data", "no data", "no data", "no data"};


            List<List<String>> lists = new ArrayList<List<String>>();
            lists.add(Arrays.asList(row));

            dayInfoArrayAdapter = new CustomAdapter(getActivity(), lists);

            dayInfoListView.setAdapter(dayInfoArrayAdapter);

            new DayInfoLoader(getActivity()).execute(new Integer[]{0});

            return rootView;
        }

        public class CustomAdapter extends ArrayAdapter<List<String>> {

            Context context;
            List<List<String>> lists;

            public CustomAdapter(Context context, List<List<String>> lists) {
                super(context, R.layout.day_schedule, lists);
                this.context = context;
                this.lists = lists;
            }

            public class Holder {
                TextView name;
                TextView interval;
                TextView special;
                TextView desc;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View rowView = convertView;

                if (rowView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    rowView = inflater.inflate(R.layout.day_schedule, null);
                    Holder holder = new Holder();

                    holder.name = (TextView) rowView.findViewById(R.id.name_tv);
                    holder.interval = (TextView) rowView.findViewById(R.id.interval);
                    holder.special = (TextView) rowView.findViewById(R.id.special);
                    holder.desc = (TextView) rowView.findViewById(R.id.desc);

                    rowView.setTag(holder);
                }

                Holder holder = (Holder) rowView.getTag();
                holder.name.setText(lists.get(position).get(0));
                holder.interval.setText(lists.get(position).get(1));
                holder.special.setText(lists.get(position).get(2));
                holder.desc.setText(lists.get(position).get(3));

                return rowView;
            }
        }

        public class DayInfoLoader extends AsyncTask<Integer, Void, String[][]> {
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
            protected String[][] doInBackground(Integer... params) {
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

                String[][] result = new String[4][4];
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray slots = jsonObject.getJSONArray("slots");

                    for(int slotNo = 0; slotNo < slots.length(); slotNo ++) {
                        JSONObject slot = (JSONObject) slots.get(slotNo);
                        String name = slot.getString("name");
                        String startTime = slot.getString("startTime");
                        int span = slot.getInt("span");
                        JSONObject foodInfo = slot.getJSONObject("foodInfo");
                        String description = foodInfo.getString("description");
                        String specialName = "";

                        if (foodInfo.has("special")) {
                            specialName = foodInfo.getString("special");
                        }

                        result[slotNo][0] = name;
                        result[slotNo][1] = processTime(startTime, span);
                        result[slotNo][2] = specialName ;
                        result[slotNo][3] = description.length() < 10 ? description : description.substring(10);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                return result;
            }

            public String processTime(String startTime, int span) {
                String[] strs = startTime.split(":");
                String hours = strs[0];
                String minutes = strs[1];

                return startTime + " to " +  Integer.valueOf(Integer.valueOf(hours) + span) + ":" + minutes;
            }

            @Override
            protected void onPostExecute(String results[][]) {
                super.onPostExecute(results);
                if (dialog != null && dialog.isShowing()) dialog.dismiss();
                if (results != null) {
                    dayInfoArrayAdapter.clear();
                    for(String[] result : results) {
                        for(String str : result) Log.d(LOG_TAG, str);
                        dayInfoArrayAdapter.add(Arrays.asList(result));
                    }
                } else {
                    Log.d(LOG_TAG, "result is not available");
                }
            }
        }
    }
}
