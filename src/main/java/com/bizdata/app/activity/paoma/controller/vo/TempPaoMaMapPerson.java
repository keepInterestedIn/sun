package com.bizdata.app.activity.paoma.controller.vo;

import lombok.Data;

@Data
public class TempPaoMaMapPerson {
	private String id;
	/**
	 * 奖品id
	 */
	private String prizeId;

	/**
	 * 排名的开始
	 */
	private String ranking;

	/**
	 * 排名的结束
	 */
	private String rankingEnd;

	/**
	 * 对应的活动id
	 */
	private String activityId;

	/**
	 * 奖品对应的图片
	 */
	private String picUrl;

	/**
	 * 奖励名称
	 */
	private String prizeName;
}
