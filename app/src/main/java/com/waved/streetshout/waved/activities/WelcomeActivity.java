package com.waved.streetshout.waved.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.waved.streetshout.waved.R;

public class WelcomeActivity extends Activity {

    private Button startButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        startButton = (Button) findViewById(R.id.welcome_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, PhoneValidationActivity.class);
                startActivity(intent);
            }
        });
    }
}
