package com.johnnyfivedev.shadowlayout;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import com.johnnyfivedev.ShadowLayout;

public class MainActivity extends AppCompatActivity {

    private ShadowLayout shadowLayout;
    private Button button;
    private View root;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.test_layout);

        button = findViewById(R.id.button);
        shadowLayout = findViewById(R.id.shadow_layout);
        root = findViewById(R.id.root);

        button.setOnClickListener(buttonOnClickListener);
        button.setOnLongClickListener(buttonLongClickListener);
    }

    private View.OnClickListener buttonOnClickListener = v -> {
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shadowLayout.setElevationEnabled(true);
        }*/

        ResizeAnimation resizeAnimation = new ResizeAnimation(button, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics()));
        resizeAnimation.setDuration(500);
        button.startAnimation(resizeAnimation);
    };

    private View.OnLongClickListener buttonLongClickListener = v -> {
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            shadowLayout.setElevationEnabled(false);
        }*/

        return true;
    };
}