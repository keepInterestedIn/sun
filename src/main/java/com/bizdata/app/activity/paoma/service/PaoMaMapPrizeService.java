package com.bizdata.app.activity.paoma.service;

import java.util.List;

import com.bizdata.app.activity.paoma.domain.PaoMaMapPrize;

public interface PaoMaMapPrizeService {
	/**
	 * 执行更新操作
	 *
	 * @param PaoMaMapPrize
	 *            奖励实体
	 * @return 执行反馈
	 */
	boolean saveOrUpdate(PaoMaMapPrize paoMaMapPrize);

	List<PaoMaMapPrize> findByActivityId(String id);

	/**
	 * 根据主键查找对象
	 * 
	 * @param id
	 * @return
	 */
	PaoMaMapPrize findById(String id);

	/**
	 * 根据主键删除对象 PS:前提是没有外键存在
	 * 
	 * @param id
	 * @return
	 */
	void del(String id);
}
