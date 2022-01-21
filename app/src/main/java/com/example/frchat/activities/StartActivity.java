package com.example.frchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.frchat.R;
import com.example.frchat.Utilities.Constants;
import com.example.frchat.Utilities.PreferenceManager;
import com.example.frchat.databinding.ActivityMainBinding;
import com.example.frchat.databinding.ActivityStartBinding;

public class StartActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 4000;
    //    Animation topAnimation, bottomAnimation;
    TextView textView;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        textView = findViewById(R.id.textView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                preferenceManager = new PreferenceManager(getApplicationContext());
                if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_SCREEN);
////        Animation
//        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
//        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
//
////        setAnimation
//        binding.imageView.setAnimation(topAnimation);
//        binding.textView.setAnimation(bottomAnimation);


    }
}