package com.vlist.hola;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StarterActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starter_activity);

        float size = 50.0f;

        TextView mText = (TextView) findViewById(R.id.ads);
        mText.setTextSize(size);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent;
        if (user != null) {
            intent = new Intent(StarterActivity.this, MainActivity.class);
        } else {
            intent = new Intent(StarterActivity.this, ChooseLoginRegistrationActivity.class);
        }

        long delayTime = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, delayTime);


    }
}

