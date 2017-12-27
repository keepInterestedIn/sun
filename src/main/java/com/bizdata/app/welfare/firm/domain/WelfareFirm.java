package com.bizdata.app.welfare.firm.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.sdevil507.base.JpaUUIDBaseEntity;

/**
 * 企业福利
 *
 */
@Table(name = "welfate_firm")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class WelfareFirm extends JpaUUIDBaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3284938875667901312L;

	/**
	 * 福利名称
	 */
	@Column
	private String welfareName;

	/**
	 * 福利类型
	 */
	@Column
	private Integer welfareType;

	/**
	 * 福利内容
	 */
	@Column
	private String content;

	/**
	 * 预约人数
	 */
	@Transient
	private Integer people = 0;

	/**
	 * 福利类型名称
	 */
	@Transient
	private String welfareTypeName;

	/**
	 * 是否置顶
	 * <p>
	 * 1表示置顶，0表示不置顶
	 */
	@Column(nullable = false, columnDefinition = "varchar(2) default '0'")
	private String isTop;
}
