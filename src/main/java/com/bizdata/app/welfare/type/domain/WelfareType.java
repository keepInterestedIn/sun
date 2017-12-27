package com.bizdata.app.welfare.type.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

/**
 * 福利类型
 *
 */
@Table(name = "welfate_type")
@Entity
@Data
public class WelfareType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	/**
	 * 福利类型
	 */
	@Column
	private String typeName;
	
	/**
	 * 提交信息,多选框,其中用复选框选择
	 * 1.无需提交2.城市3.医院4.科室5.医生6.就诊时间7.怀孕周数
	 */
	@Column
	private String commit;
	
	/**
	 * 复选框是否已经被选中
	 */
	@Transient
	private List<Boolean> isChecked;
}
