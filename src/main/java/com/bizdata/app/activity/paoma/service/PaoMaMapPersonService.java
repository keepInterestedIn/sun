package com.bizdata.app.activity.paoma.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.bizdata.app.activity.paoma.controller.vo.PaoMaMapPersonVO;
import com.bizdata.app.activity.paoma.domain.PaoMaMapPerson;

import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;

public interface PaoMaMapPersonService {
	/**
	 * 分页查询
	 *
	 * @param jpaPageParamVO
	 *            分页参数
	 * @param jpaSortParamVO
	 *            排序参数
	 * @param PaoMaMapPersonVO
	 *            跑马圈查询参数VO
	 * @return 分页记录
	 */
	Page<PaoMaMapPerson> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO,
			PaoMaMapPersonVO paoMaMapPersonVO);

	/**
	 * 根据活动Id来获取该活动下的参赛人员
	 * 
	 * @param id
	 * @return
	 */
	int getPeople(String id);

	/**
	 * 参加活动的用户的极光推送设备id
	 * 
	 * @param n
	 * @return
	 */
	List<String> getJpushid(String n);

	/**
	 * 根据用户id和活动id获取排名
	 * 
	 * @param activityId
	 * @param userId
	 * @return
	 */
	Integer getRankByActivityIdAndUserId(String activityId, String userId);

}
