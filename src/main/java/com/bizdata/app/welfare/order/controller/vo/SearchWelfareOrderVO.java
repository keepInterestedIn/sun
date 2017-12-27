package com.bizdata.app.welfare.order.controller.vo;

import java.util.Date;

import lombok.Data;

@Data
public class SearchWelfareOrderVO {
	public SearchWelfareOrderVO(String welfareType, Date startDate, Date endDate) {
		this.welfareType = welfareType;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public SearchWelfareOrderVO(){
		
	}
	/**
	 * 福利主键
	 */
	private String welfareId;

	private String nickName;

	private String realName;

	private String mobile;

	/**
	 * 证件类型 10-居民身份证，11-居民户口簿，12-驾驶证，13-军官证，16-异常身份证，
	 * <p>
	 * 17-台湾居民来往大陆通行证，19-港澳居民来往内地通行证，21-组织机构代码证，51-护照，99-其他证件
	 */
	private String idType;

	/**
	 * 证件号码
	 */
	private String idNum;

	private String city;

	private String hospital;

	/**
	 * 科室
	 */
	private String departments;

	/**
	 * 预约类型id
	 */
	private String welfareType;

	/**
	 * 预约开始时间
	 */
	private Date startDate;

	/**
	 * 预约结束时间
	 */
	private Date endDate;
}
