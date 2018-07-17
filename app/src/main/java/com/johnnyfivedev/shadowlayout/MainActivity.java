package com.johnnyfivedev.shadowlayout;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    shadowLayout.setElevationEnabled(true);
                }*/

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) shadowLayout.getLayoutParams();
                params.width = 200;
                shadowLayout.setLayoutParams(params);
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