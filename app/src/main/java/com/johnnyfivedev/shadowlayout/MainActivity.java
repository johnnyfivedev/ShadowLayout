package com.johnnyfivedev.shadowlayout;

import android.animation.StateListAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.johnnyfivedev.ShadowLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.test_layout);
        Button button = findViewById(R.id.button);
        final ShadowLayout shadowLayout = findViewById(R.id.shadow_layout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    shadowLayout.setElevationEnabled(true);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((ShadowLayout) findViewById(R.id.shadow_layout)).setElevationEnabled(false);
        }
    }
}