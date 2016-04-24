package com.tf.truefeeling.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.tf.truefeeling.R;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Intent mainIntent = new Intent(this, MainTabActivity.class);
        final Intent loginIntent = new Intent(this,LoginActivity.class);

        TextView loginButton=(TextView)findViewById(R.id.mi_login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AVUser.getCurrentUser()!=null){
                    startActivity(mainIntent);
                }
                else{
                    startActivity(loginIntent);
                }
            }
        });
    }

}
