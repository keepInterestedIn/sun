package com.bizdata.app.welfare.firm.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bizdata.app.welfare.firm.controller.vo.SearchWelfareFirmVO;
import com.bizdata.app.welfare.firm.domain.WelfareFirm;
import com.bizdata.app.welfare.firm.repository.WelfareFirmRepository;
import com.bizdata.app.welfare.firm.service.WelfareFirmService;
import com.bizdata.commons.utils.KindeditorIgnoreHtml;

import lombok.extern.slf4j.Slf4j;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;

@Service
@Slf4j
public class WelfareFirmServiceImpl implements WelfareFirmService {
	@Autowired
	private WelfareFirmRepository welfareFirmRepository;

	@Override
	public Page<WelfareFirm> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO,
			SearchWelfareFirmVO searchWelfareFirmVO) {
		return welfareFirmRepository.findAll(listWhereClause(searchWelfareFirmVO),
				jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));

	}

	private Specification<WelfareFirm> listWhereClause(SearchWelfareFirmVO searchWelfareFirmVO) {
		return new Specification<WelfareFirm>() {
			@Override
			public Predicate toPredicate(Root<WelfareFirm> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<>();
				// 如果福利类型不为空
				if (StringUtils.isNotBlank(searchWelfareFirmVO.getWelfareType())) {
					list.add(criteriaBuilder.equal(root.get("welfareType").as(Integer.class),
							searchWelfareFirmVO.getWelfareType()));
				}
				
				// 如果福利名称不为空
				if (StringUtils.isNotBlank(searchWelfareFirmVO.getWelfareName())) {
					list.add(criteriaBuilder.like(root.get("welfareName").as(String.class),
							"%" + searchWelfareFirmVO.getWelfareName() + "%"));
				}

				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));
			}
		};

	}

	@Override
	public WelfareFirm findOne(String id) {
		return welfareFirmRepository.findOne(id);
	}

	@Override
	public boolean save(WelfareFirm welfareFirm) {
		boolean state;
		try {
			welfareFirm = KindeditorIgnoreHtml.unescapeHtml(welfareFirm, "content");
			welfareFirmRepository.save(welfareFirm);
			state = true;
		} catch (Exception e) {
			log.error("保存或更新welfareFirm失败:", e);
			state = false;
		}
		return state;
	}

	@Override
	public boolean delete(String id) {
		boolean state;
		try {
			welfareFirmRepository.delete(id);
			state = true;
		} catch (Exception e) {
			log.error("welfareFirm删除失败:", e);
			state = false;
		}
		return state;
	}
}
