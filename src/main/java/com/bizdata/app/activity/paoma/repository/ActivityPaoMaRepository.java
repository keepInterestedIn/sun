package com.bizdata.app.activity.paoma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.bizdata.app.activity.paoma.domain.PaoMa;

import me.sdevil507.base.JpaBaseRepository;

public interface ActivityPaoMaRepository extends JpaBaseRepository<PaoMa, String>  {

	/**
	 * 获取最大序号
	 * @return
	 */
	@Query(value = "select a.activity_no from activity_paoma as a order by a.activity_no desc limit 1", nativeQuery = true)
	String getMaxNO();
	/**
	 * 获取最新的活动多个id
	 * @param num
	 * @return
	 */
	@Query(value = "select id from activity_paoma where state = '0' order by end_date desc limit ?1", nativeQuery = true)
	List<String> getLastids(int num);

}
