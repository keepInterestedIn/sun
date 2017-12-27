package com.bizdata.app.activity.paoma.controller.vo;

import com.bizdata.app.activity.paoma.domain.PaoMa;

import lombok.Data;

/**
 * 编辑页面才会用到
 * 
 * @author w
 *
 */
@Data
public class EditPaoMaVO {
	// 跑马圈活动
	private PaoMa paoMa;

	// 活动中的有关奖励的定义
	private TempPaoMaMapPerson paoMaMapPrizes;
}
