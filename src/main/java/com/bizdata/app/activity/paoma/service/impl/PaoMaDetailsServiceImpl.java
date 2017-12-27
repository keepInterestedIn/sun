package com.bizdata.app.activity.paoma.service.impl;

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

import com.bizdata.app.activity.paoma.controller.vo.PaoMaMapPersonVO;
import com.bizdata.app.activity.paoma.domain.PaoMaDetails;
import com.bizdata.app.activity.paoma.repository.PaoMaDetailsRepository;
import com.bizdata.app.activity.paoma.service.PaoMaDetailsService;

import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;

@Service
public class PaoMaDetailsServiceImpl implements PaoMaDetailsService {

	private final PaoMaDetailsRepository paoMaDetailsRepository;

	@Autowired
	public PaoMaDetailsServiceImpl(PaoMaDetailsRepository paoMaDetailsRepository) {
		this.paoMaDetailsRepository = paoMaDetailsRepository;
	}

	@Override
	public List<PaoMaDetails> findByActivityId(String activityId) {
		return paoMaDetailsRepository.findByActivityId(activityId);
	}

	@Override
	public Page<PaoMaDetails> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO,
			PaoMaMapPersonVO paoMaMapPersonVO) {
		String activityId = paoMaMapPersonVO.getId();
		String nickName = paoMaMapPersonVO.getNickName();
		String mobile = paoMaMapPersonVO.getMobile();
		if (StringUtils.isNotEmpty(nickName)) {
			nickName = "%" + nickName + "%";
		}
		// 活动为空
		if (activityId == null) {
			return paoMaDetailsRepository.findAll(jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));
		}
		// 活动不为空
		if (activityId != null) {
			// 活动和昵称不为空
			if (nickName != null && mobile == null) {
				return paoMaDetailsRepository.findAll(jpaPageParamVO.getPageable(jpaSortParamVO.getSort()), activityId,
						nickName);
			}
			// 活动和手机号码不为空
			if (mobile != null && nickName == null) {
				return paoMaDetailsRepository.findAll(jpaPageParamVO.getPageable(jpaSortParamVO.getSort()), activityId,
						mobile);
			}
			// 活动和手机号码以及昵称不为空
			if (nickName != null && mobile != null) {
				return paoMaDetailsRepository.findAll(jpaPageParamVO.getPageable(jpaSortParamVO.getSort()), activityId,
						nickName, mobile);
			}
		}
		return paoMaDetailsRepository.findAll(listWhereClause(activityId),
				jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));
	}

	private Specification<PaoMaDetails> listWhereClause(String activityId) {
		return new Specification<PaoMaDetails>() {
			@Override
			public Predicate toPredicate(Root<PaoMaDetails> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<>();
				list.add(criteriaBuilder.equal(root.get("activityId").as(String.class), activityId));

				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));
			}
		};
	}

	@Override
	public String findPrizeName(String activityId, Integer ranking) {
		return paoMaDetailsRepository.getPrizeId(ranking, activityId);
	}
}
