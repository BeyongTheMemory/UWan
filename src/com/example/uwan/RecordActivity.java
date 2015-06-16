package com.example.uwan;

import java.util.ArrayList;
import java.util.List;

import com.example.uwan.adapte.StatusExpandAdapter;
import com.example.uwan.bean.ChildStatusEntity;
import com.example.uwan.bean.GroupStatusEntity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;



public class RecordActivity extends Activity {

	private ExpandableListView expandlistView;
	private StatusExpandAdapter statusAdapter;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_activity);
		context = this;
		expandlistView = (ExpandableListView) findViewById(R.id.expandlist);
		initExpandListView();
	}

	/**
	 * ��ʼ������չ�б�
	 */
	private void initExpandListView() {
		statusAdapter = new StatusExpandAdapter(context, getListData());
		expandlistView.setAdapter(statusAdapter);
		expandlistView.setGroupIndicator(null); // ȥ��Ĭ�ϴ��ļ�ͷ
		expandlistView.setSelection(0);// ����Ĭ��ѡ����

		// ��������group,�����������ó�Ĭ��չ��
		int groupCount = expandlistView.getCount();
		for (int i = 0; i < groupCount; i++) {
			expandlistView.expandGroup(i);
		}

		expandlistView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

	private List<GroupStatusEntity> getListData() {
		List<GroupStatusEntity> groupList;
		String[] strArray = new String[] { "6��22��", "6��23��", "6��25��" };
		String[][] childTimeArray = new String[][] {
				{ "ע��UWan", "�ղ�����:xxx", "��������:xxx", "����������Ϣ:xxx" },
				{ "��������:xx" }, { "�յ��ظ�:xxx" } };
		groupList = new ArrayList<GroupStatusEntity>();
		for (int i = 0; i < strArray.length; i++) {
			GroupStatusEntity groupStatusEntity = new GroupStatusEntity();
			groupStatusEntity.setGroupName(strArray[i]);

			List<ChildStatusEntity> childList = new ArrayList<ChildStatusEntity>();

			for (int j = 0; j < childTimeArray[i].length; j++) {
				ChildStatusEntity childStatusEntity = new ChildStatusEntity();
				childStatusEntity.setCompleteTime(childTimeArray[i][j]);
				childStatusEntity.setIsfinished(true);
				childList.add(childStatusEntity);
			}

			groupStatusEntity.setChildList(childList);
			groupList.add(groupStatusEntity);
		}
		return groupList;
	}
}