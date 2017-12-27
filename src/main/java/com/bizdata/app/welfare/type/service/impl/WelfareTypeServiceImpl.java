package com.bizdata.app.welfare.type.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bizdata.app.welfare.type.controller.vo.SearchWelfareVO;
import com.bizdata.app.welfare.type.domain.WelfareType;
import com.bizdata.app.welfare.type.repository.WelfareTypeRepository;
import com.bizdata.app.welfare.type.service.WelfareTypeService;

import lombok.extern.slf4j.Slf4j;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;

@Service
@Slf4j
public class WelfareTypeServiceImpl implements WelfareTypeService {
	@Autowired
	private WelfareTypeRepository welfareTypeRepository;

	@Override
	public Page<WelfareType> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO,
			SearchWelfareVO searchWelfareVO) {
		return welfareTypeRepository.findAll(listWhereClause(searchWelfareVO),
				jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));

	}

	private Specification<WelfareType> listWhereClause(SearchWelfareVO searchWelfareVO) {
		return new Specification<WelfareType>() {
			@Override
			public Predicate toPredicate(Root<WelfareType> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<>();
				// 如果福利类型不为空
				if (StringUtils.isNotBlank(searchWelfareVO.getWelfareType())) {
					list.add(criteriaBuilder.like(root.get("typeName").as(String.class),
							"%" + searchWelfareVO.getWelfareType() + "%"));
				}

				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));
			}
		};
	}

	@Override
	public WelfareType findOne(String id) {
		return welfareTypeRepository.findOne(Integer.valueOf(id));
	}

	@Override
	public boolean save(WelfareType welfareType) {
		boolean state;
		try {
			welfareTypeRepository.save(welfareType);
			state = true;
		} catch (Exception e) {
			log.error("保存或更新welfareType失败:", e);
			state = false;
		}
		return state;
	}

	@Override
	public boolean delete(String id) {
		boolean state;
		try {
			welfareTypeRepository.delete(Integer.valueOf(id));
			state = true;
		} catch (Exception e) {
			log.error("welfareType删除失败:", e);
			state = false;
		}
		return state;
	}

	@Override
	public Map<Integer, String> findNameAndId() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		List<Object> ss = welfareTypeRepository.findDeviceCataNameAndId();
		if (ss == null) {
			return null;
		}
		for (int i = 0; i < ss.size(); i++) {
			Object[] kk = (Object[]) ss.get(i);
			map.put(Integer.valueOf(kk[0].toString()), kk[1].toString());
		}
		return map;
	}

	@Override
	public String getTypeNameById(String welfareTypeId) {
		return welfareTypeRepository.getTypeNameById(Integer.valueOf(welfareTypeId));
	}

}
