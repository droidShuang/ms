package com.junova.ms.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.junova.ms.R;
import com.junova.ms.app.AppConfig;
import com.junova.ms.main.MainActivity;
import com.junova.ms.utils.PrefUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity {
    @Bind(R.id.layout_start)
    RelativeLayout startLayout;
    private AlphaAnimation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        init();
        goMainOrLoginActivity();
    }

    private void goMainOrLoginActivity() {
        animation = new AlphaAnimation(0.5f, 1.0f);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startLayout.startAnimation(animation);
    }

    private void startActivity() {
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(PrefUtils.getString(this, "numberCode", ""))) {
            intent.setClass(StartActivity.this, MainActivity.class);

        } else {
            intent.setClass(StartActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        this.finish();
    }

    private void init() {
        ButterKnife.bind(StartActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(StartActivity.this);
    }
}
