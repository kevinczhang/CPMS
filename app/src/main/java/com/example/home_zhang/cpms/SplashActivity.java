package com.example.home_zhang.cpms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by czhang on 6/10/2017.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
