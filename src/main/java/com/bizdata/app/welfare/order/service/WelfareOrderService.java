package com.bizdata.app.welfare.order.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.bizdata.app.welfare.order.controller.vo.SearchWelfareOrderVO;
import com.bizdata.app.welfare.order.domain.WelfareOrder;

import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;

public interface WelfareOrderService {

	Page<WelfareOrder> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO,
			SearchWelfareOrderVO searchWelfareOrderVO);

	List<WelfareOrder> findAll();

	Integer getPeopleByWelfare(String id);
	//导出查找
	List<WelfareOrder> findAll(SearchWelfareOrderVO searchWelfareOrderVO);

}
