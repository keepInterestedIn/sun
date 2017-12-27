package com.bizdata.app.welfare.firm.service;

import org.springframework.data.domain.Page;

import com.bizdata.app.welfare.firm.controller.vo.SearchWelfareFirmVO;
import com.bizdata.app.welfare.firm.domain.WelfareFirm;

import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;

public interface WelfareFirmService {

	Page<WelfareFirm> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO,
			SearchWelfareFirmVO searchWelfareFirmVO);

	WelfareFirm findOne(String id);

	boolean save(WelfareFirm welfareFirm);

	boolean delete(String id);

}
