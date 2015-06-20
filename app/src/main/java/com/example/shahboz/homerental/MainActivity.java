package com.example.shahboz.homerental;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.shahboz.homerental.data.MyUser;
import com.example.shahboz.homerental.ui.AdvertiserHomeActivity;
import com.example.shahboz.homerental.ui.StudentHomeActivity;
import com.parse.ParseUser;


public class MainActivity extends ActionBarActivity {

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if there is current user info
        MyUser currentUser = (MyUser) ParseUser.getCurrentUser();
        if (currentUser != null) {
            if(currentUser.getType().equals(MyApp.STUDENT)){
                //goto student main page activity
                Intent intent = new Intent(this, StudentHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }


            else {
                Intent intent = new Intent(this, AdvertiserHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            //goto advertiser main page
            }


        } else {
            // Start and intent for the logged out activity
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
