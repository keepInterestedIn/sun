package com.bizdata.app.welfare.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.bizdata.app.welfare.order.domain.WelfareOrder;

import me.sdevil507.base.JpaBaseRepository;

public interface WelfareOrderRepository extends JpaBaseRepository<WelfareOrder, String> {

	@Query(value = "select count(*) from welfate_order where welfare_id = ?1", nativeQuery = true)
	Integer getPeopleByWelfare(String id);

	@Query(value = "select a from WelfareOrder as a, MagUser as b, UserProfile as c where a.userId = b.id and a.userId = c.accountId and (b.mobile = ?1 or c.nickName = ?1) and a.welfareId = ?2")
	Page<WelfareOrder> findAll(Pageable pageable, String mobile, String welfareId);

	@Query(value = "select a from WelfareOrder as a, MagUser as b, UserProfile as c where a.userId = b.id and a.userId = c.accountId and (b.mobile = ?1 or c.nickName = ?1)")
	Page<WelfareOrder> findAll(Pageable pageable, String mobile);

}
