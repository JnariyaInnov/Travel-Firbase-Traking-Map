package me.varunon9.saathmetravel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import me.varunon9.saathmetravel.constants.AppConstants;
import me.varunon9.saathmetravel.ui.chat.ChatFragment;
import me.varunon9.saathmetravel.ui.chat.ChatListFragment;
import me.varunon9.saathmetravel.ui.chat.ProfileFragment;
import me.varunon9.saathmetravel.utils.FirestoreDbUtility;

public class ChatFragmentActivity extends AppCompatActivity {

    private String TAG = "ChatFragmentActivity";
    private ProgressDialog progressDialog;
    public FirestoreDbUtility firestoreDbUtility;
    public String userUid;
    public String travellerUserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");
        setContentView(R.layout.chat_activity);

        // display back button in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestoreDbUtility = new FirestoreDbUtility();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            int navigationLink = bundle.getInt(AppConstants.NAVIGATION_ITEM);
            userUid = bundle.getString(AppConstants.USER_UID);
            travellerUserUid = bundle.getString(AppConstants.TRAVELLER_USER_UID);
            Fragment fragment = getSelectedFragment(navigationLink);
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commitNow();
            } else {
                Log.e(TAG, "Null Fragment to display");
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private Fragment getSelectedFragment(int id) {
        Fragment fragment = null;
        String title = "";
        if (id == R.id.nav_profile) {
            title = AppConstants.ChatFragmentActivityTitle.PROFILE;
            fragment = new ProfileFragment();
        } else if (id == R.id.nav_chats) {
            title = AppConstants.ChatFragmentActivityTitle.Chats;
            fragment = new ChatListFragment();
        }
        updateActionBarTitle(title);
        return fragment;
    }

    public void updateActionBarTitle(String title) {
        if (title != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void showProgressDialog(String title, String message, boolean isCancellable) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ChatFragmentActivity.this);
        }
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(isCancellable);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showMessage(String message) {
        View parentLayout = findViewById(R.id.container);
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show();
    }

    public void goToChatFragment() {
        Fragment fragment = new ChatFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow();
    }
}
