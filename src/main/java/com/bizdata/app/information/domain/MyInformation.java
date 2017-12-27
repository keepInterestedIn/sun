package com.bizdata.app.information.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.sdevil507.base.JpaUUIDBaseEntity;

/**
 * 消息中心中的我的消息
 *
 */
@Entity
@Table(name = "app_my_information")
@Data
@EqualsAndHashCode(callSuper = true)
public class MyInformation extends JpaUUIDBaseEntity {

	/**
	 * 消息内容
	 */
	@Column
	private String content;

	/**
	 * 给谁的
	 */
	@Column
	private String profileId;

	/**
	 * 活动id（方便跳转）
	 */
	@Column
	private String activityId;

	/**
	 * 是否已经阅读
	 */
	@Column
	private String isRead;

	@Column
	private Date pushDate;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public Date getPushDate() {
		return pushDate;
	}

	public void setPushDate(Date pushDate) {
		this.pushDate = pushDate;
	}

}
