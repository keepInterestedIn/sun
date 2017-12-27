package com.bizdata.app.app_mag.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.sdevil507.base.JpaUUIDBaseEntity;

@Entity
@Table(name = "app_app_mag")
@Data
@EqualsAndHashCode(callSuper = true)
public class App_mag extends JpaUUIDBaseEntity {
	/*
	 * ID
	 */
	@Column
	private int magid;
	/*
	 * 平台号
	 * 写死Android:Android,IOS:IOS
	 * 待修改为动态
	 */
	@Column
	private String platformnum;
	/*
	 * 版本号
	 * 写死只有1.0,1.1
	 * 待修改为动态
	 */
	@Column
	private String versionnum;
	/*
	 * 类型
	 * PATCH:补丁
	 * SKIN:皮肤
	 */
	@Column
	private String type;
	/*
	 * 下载地址
	 */
	@Column
	private String downaddress;
	/*
	 * 有效时间起
	 */
	@Column
	private Date startdate;
	/*
	 * 有效时间止
	 */
	@Column
	private Date enddate;
	/*
	 * 状态
	 * 0:正常,1:下线
	 */
	@Column
	private String state = "1";
	/*
	 * 创建时间
	 */
	@Column
	private Date createdate;
}