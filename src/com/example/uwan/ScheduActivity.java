package com.example.uwan;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ScheduActivity extends Activity {
        
        /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //取消标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.shedu_activity);
        
      //  ImageView mImageView = (ImageView)findViewById(R.id.welcomimageView1);
        LinearLayout ll_welcome = (LinearLayout)findViewById(R.id.ll_welcome);
        AlphaAnimation mAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        mAlphaAnimation.setDuration(3000);
        ll_welcome.setAnimation(mAlphaAnimation);
        
        mAlphaAnimation.setAnimationListener(new AnimationListener() {
                        
                        @Override
                        public void onAnimationStart(Animation animation) {
                                // TODO Auto-generated method stub
                                
                        }
                        
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                                // TODO Auto-generated method stub
                                
                        }
                        
                        @Override
                        public void onAnimationEnd(Animation animation) {
                                // TODO Auto-generated method stub
                                Intent mIntent = new Intent(ScheduActivity.this, MainActivity.class);
                                startActivity(mIntent);       
                                ScheduActivity.this.finish();
                        }
                });
    }
}