package com.iit_mandi.android.hassle;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public static final String TAG = PlaceholderFragment.class.getName();

        private LoginButton loginButton;
        private UiLifecycleHelper uiLifecycleHelper;
        private TextView username;

        public PlaceholderFragment() {
        }

        private Session.StatusCallback statusCallback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if(state.isOpened()) {
                    Log.d(TAG, "facebook session is open");
                } else if(state.isClosed()) {
                    Log.d(TAG, "facebook session is closed");
                }
            }
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            uiLifecycleHelper = new UiLifecycleHelper(getActivity(), statusCallback);
            uiLifecycleHelper.onCreate(savedInstanceState);

            loginButton = (LoginButton) rootView.findViewById(R.id.authButton);
            loginButton.setFragment(this);
            loginButton.setReadPermissions(Arrays.asList("email"));

            loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
                @Override
                public void onUserInfoFetched(GraphUser user) {
                    if(user != null) {
                        Log.d(TAG, "first name: " + user.getFirstName());
                        Log.d(TAG, "last name: " + user.getLastName());
                        Log.d(TAG, "email: " + user.getProperty("email"));
                        startActivity(new Intent(getActivity(), HassleActivity.class));
                    } else {
                        Log.d(TAG, "user not logged in");
                    }
                }
            });

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            uiLifecycleHelper.onResume();
            AppEventsLogger.activateApp(getActivity());
        }

        @Override
        public void onPause() {
            super.onPause();
            uiLifecycleHelper.onPause();
            AppEventsLogger.deactivateApp(getActivity());
        }


        @Override
        public void onStop() {
            super.onStop();
            uiLifecycleHelper.onStop();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            uiLifecycleHelper.onDestroy();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            uiLifecycleHelper.onActivityResult(requestCode, resultCode, data);
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            uiLifecycleHelper.onSaveInstanceState(outState);
        }
    }
}
