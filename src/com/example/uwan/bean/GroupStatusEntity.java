package com.example.uwan.bean;

import java.util.List;

/**
 * һ��Itemʵ����
 * 
 * @author zihao
 * 
 */
public class GroupStatusEntity {
	private String groupName;
	/** ����Item�����б� **/
	private List<ChildStatusEntity> childList;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<ChildStatusEntity> getChildList() {
		return childList;
	}

	public void setChildList(List<ChildStatusEntity> childList) {
		this.childList = childList;
	}

}