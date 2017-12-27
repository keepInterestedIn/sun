package com.bizdata.app.welfare.type.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.bizdata.app.welfare.type.controller.vo.SearchWelfareVO;
import com.bizdata.app.welfare.type.domain.WelfareType;

import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;

public interface WelfareTypeService {

	Page<WelfareType> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO,
			SearchWelfareVO searchWelfareVO);

	WelfareType findOne(String id);

	boolean save(WelfareType welfareType);

	boolean delete(String id);

	Map<Integer, String> findNameAndId();

	String getTypeNameById(String welfareTypeId);

}
