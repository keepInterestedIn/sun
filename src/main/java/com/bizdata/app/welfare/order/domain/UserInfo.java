package com.bizdata.app.welfare.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.sdevil507.base.JpaUUIDBaseEntity;

/**
 * 员工信息
 *
 */
@Table(name = "usr_employee")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfo extends JpaUUIDBaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4973938616450298184L;

	/**
	 * 用户id
	 */
	@Column
	private String userId;

	/**
	 * 姓名
	 */
	@Column
	private String realName;

	/**
	 * 证件类型
	 * <p>
	 * 1:身份证2:护照
	 */
	@Column
	private String idType;

	/**
	 * 证件号
	 */
	@Column
	private String idNum;
}
