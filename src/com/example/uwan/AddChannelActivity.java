package com.example.uwan;

import java.util.ArrayList;
import java.util.List;

import com.example.uwan.view.FlowLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddChannelActivity extends Activity implements OnClickListener {

	FlowLayout tag_vessel;
	ImageView add_tag;
	List<String> mTagList = new ArrayList<String>();
	int screenWidth = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addchannel);
		initView();

	}

	public void initView() {
		screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素
		tag_vessel = (FlowLayout) findViewById(R.id.tag_vessel);
		add_tag = (ImageView) findViewById(R.id.add_tag);
		add_tag.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_tag:
			// 添加标签
			AddTagDialog();
			break;
		}
	}

	/**
	 * 添加标签的对话框
	 */
	public void AddTagDialog() {
		final Dialog dlg = new Dialog(AddChannelActivity.this, R.style.dialog);
		dlg.show();
		dlg.getWindow().setGravity(Gravity.CENTER);
		dlg.getWindow().setLayout((int) (screenWidth * 0.8), android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		dlg.getWindow().setContentView(R.layout.setting_add_tags_dialg);
		TextView add_tag_dialg_title = (TextView) dlg.findViewById(R.id.add_tag_dialg_title);
		final EditText add_tag_dialg_content = (EditText) dlg.findViewById(R.id.add_tag_dialg_content);
		TextView add_tag_dialg_no = (TextView) dlg.findViewById(R.id.add_tag_dialg_no);
		TextView add_tag_dialg_ok = (TextView) dlg.findViewById(R.id.add_tag_dialg_ok);
		add_tag_dialg_title.setText("添加关键字");
		add_tag_dialg_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});
		add_tag_dialg_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//
				InputMethodManager imm = (InputMethodManager) AddChannelActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);

				mTagList.add(add_tag_dialg_content.getText().toString());
				AddTag(mTagList.get(mTagList.size() - 1), mTagList.size() - 1);
				Log.v("==", mTagList.toString() + "*");
				dlg.dismiss();
			}
		});
	}

	/**
	 * 添加标签
	 * @param tag
	 * @param i
	 */
	@SuppressLint("NewApi")
	public void AddTag(String tag, int i) {
		final TextView mTag = new TextView(AddChannelActivity.this);
		mTag.setText("  " + tag + "    ");
		// mTag.setPadding(0, 15, 40, 15);
		mTag.setGravity(Gravity.CENTER);
		mTag.setTextSize(14);
		mTag.setBackground(getResources().getDrawable(R.drawable.mylable));
		// mTag.setBackgroundColor(getResources().getColor(R.color.black));
		mTag.setTextColor(Color.GRAY);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 40);
		params.setMargins(0, 10, 20, 10);
		tag_vessel.addView(mTag, i, params);

		mTag.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// 长按标签删除操作
				final AlertDialog dlg = new AlertDialog.Builder(AddChannelActivity.this).create();
				dlg.show();
				dlg.getWindow().setGravity(Gravity.CENTER);
				dlg.getWindow().setLayout((int) (screenWidth * 0.8), android.view.WindowManager.LayoutParams.WRAP_CONTENT);
				dlg.getWindow().setContentView(R.layout.setting_add_tags_dialg);
				TextView add_tag_dialg_title = (TextView) dlg.findViewById(R.id.add_tag_dialg_title);
				EditText add_tag_dialg_content = (EditText) dlg.findViewById(R.id.add_tag_dialg_content);
				TextView add_tag_dialg_no = (TextView) dlg.findViewById(R.id.add_tag_dialg_no);
				TextView add_tag_dialg_ok = (TextView) dlg.findViewById(R.id.add_tag_dialg_ok);
				add_tag_dialg_title.setText("关键词删除确认");
				add_tag_dialg_content.setText("您确定要删除“" + mTag.getText().toString() + "”这个关键词吗？");
				add_tag_dialg_no.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dlg.dismiss();
					}
				});
				add_tag_dialg_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						tag_vessel.removeView(mTag);
						for (int j = 0; j < mTagList.size(); j++) {
							Log.v("==", mTag.getText().toString() + "==" + mTagList.get(j).toString());
							if (mTag.getText().toString().replaceAll(" ", "")
									.equals(mTagList.get(j).toString().replaceAll(" ", ""))) {
								mTagList.remove(j);
							}
						}
						dlg.dismiss();
					}
				});

				return true;
			}
		});
	}
}
