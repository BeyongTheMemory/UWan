/**
 * Copyright (c) www.longdw.com
 */
package com.example.uwan;

import com.example.uwan.option.SPStorage;
import com.example.uwan.tool.BaseTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;


/**
 * 
 * @author longdw(longdawei1988@gmail.com)
 *
 */
public class MenuSettingActivity extends Activity implements OnClickListener{
	
	private LinearLayout mAdviceLayout, mAboutLayout;
	private CheckedTextView mChangeSongTv, mAutoLyricTv, mFilterSizeTv, mFilterTimeTv;
	private SPStorage mSp;
	//private ImageButton mBackBtn;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_setting_fragment);
    	mSp = new SPStorage(this);
        initView();
    }

	private void initView() {
		mAboutLayout = (LinearLayout) findViewById(R.id.setting_about_layout);
		mAdviceLayout = (LinearLayout) findViewById(R.id.setting_advice_layout);
		mAboutLayout.setOnClickListener(this);
		mAdviceLayout.setOnClickListener(this);
		
	//	mBackBtn = (ImageButton) findViewById(R.id.backBtn);
		//mBackBtn.setOnClickListener(this);
		
		mChangeSongTv = (CheckedTextView) findViewById(R.id.shake_change_song);
		mAutoLyricTv = (CheckedTextView) findViewById(R.id.auto_download_lyric);
		mFilterSizeTv = (CheckedTextView) findViewById(R.id.filter_size);
		mFilterTimeTv = (CheckedTextView) findViewById(R.id.filter_time);
		
		mChangeSongTv.setChecked(mSp.getShake());
		mAutoLyricTv.setChecked(mSp.getAutoLyric());
		mFilterSizeTv.setChecked(mSp.getFilterSize());
		mFilterTimeTv.setChecked(mSp.getFilterTime());
		
		mChangeSongTv.setOnClickListener(this);
		mAutoLyricTv.setOnClickListener(this);
		mFilterSizeTv.setOnClickListener(this);
		mFilterTimeTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.setting_about_layout:
			break;
		case R.id.setting_advice_layout:
			break;
		case R.id.shake_change_song:
			mChangeSongTv.toggle();
			mSp.saveShake(mChangeSongTv.isChecked());
			break;
		case R.id.auto_download_lyric:
			mAutoLyricTv.toggle();
			mSp.saveAutoLyric(mAutoLyricTv.isChecked());
			break;
		case R.id.filter_size:
			mFilterSizeTv.toggle();
			mSp.saveFilterSize(mFilterSizeTv.isChecked());
			break;
		case R.id.filter_time:
			mFilterTimeTv.toggle();
			mSp.saveFilterTime(mFilterTimeTv.isChecked());
			break;
	}
}
}
