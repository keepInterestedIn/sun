package com.bizdata.app.welfare.order.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.sdevil507.base.JpaUUIDBaseEntity;

/**
 * 预约详情
 *
 */
@Table(name = "welfate_order")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class WelfareOrder extends JpaUUIDBaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3550746912664807432L;

	/**
	 * 用户昵称
	 */
	@Transient
	private String nickName;

	/**
	 * 用户姓名
	 */
	@Column
	private String realName;

	/**
	 * 手机号码
	 */
	@Transient
	private String mobile;

	/**
	 * 证件类型
	 * <p>
	 * 10-居民身份证，11-居民户口簿，12-驾驶证，13-军官证，16-异常身份证，
	 * 17-台湾居民来往大陆通行证，19-港澳居民来往内地通行证，21-组织机构代码证，51-护照，99-其他证件
	 */
	@Column
	private String idType;

	/**
	 * 证件号
	 */
	@Column
	private String idNum;

	/**
	 * 预约类型即为福利类型
	 */
	@Column
	private String welfareType;

	/**
	 * 福利类型名称
	 */
	@Transient
	private String welfareTypeName;

	/**
	 * 用户id
	 */
	@Column
	private String userId;

	/**
	 * 城市
	 */
	@Column
	private String city;

	/**
	 * 医院
	 */
	@Column
	private String hospital;

	/**
	 * 科室
	 */
	@Column
	private String departments;

	/**
	 * 怀孕周数
	 */
	@Column
	private String fetationNum;

	/**
	 * 医生
	 */
	@Column
	private String doctor;

	/**
	 * 申请时间
	 */
	@Column
	private Date createDate;

	/**
	 * 就诊时间
	 */
	@Column
	private Date seeDoctorDate;

	/**
	 * 对应的福利主键
	 */
	@Column
	private String welfareId;
}
