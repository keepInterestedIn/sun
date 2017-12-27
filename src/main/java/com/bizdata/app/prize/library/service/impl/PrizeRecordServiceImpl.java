package com.bizdata.app.prize.library.service.impl;

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

import com.bizdata.app.activity.paoma.domain.PaoMa;
import com.bizdata.app.activity.paoma.repository.ActivityPaoMaRepository;
import com.bizdata.app.prize.library.controller.vo.PrizeRecordSearchVO;
import com.bizdata.app.prize.library.domain.PrizeRecord;
import com.bizdata.app.prize.library.repository.PrizeRecordRepository;
import com.bizdata.app.prize.library.service.PrizeRecordService;

import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;

@Service
public class PrizeRecordServiceImpl implements PrizeRecordService {

	private final PrizeRecordRepository prizeRecordRepository;

	@Autowired
	public PrizeRecordServiceImpl(PrizeRecordRepository prizeRecordRepository) {
		this.prizeRecordRepository = prizeRecordRepository;
	}

	@Override
	public Page<PrizeRecord> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO,
			PrizeRecordSearchVO prizeRecordSearchVO) {
		return prizeRecordRepository.findAll(listWhereClause(prizeRecordSearchVO),
				jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));
	}

	private Specification<PrizeRecord> listWhereClause(PrizeRecordSearchVO prizeRecordSearchVO) {
		return new Specification<PrizeRecord>() {
			@Override
			public Predicate toPredicate(Root<PrizeRecord> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<>();
				// 如果对应的奖励id存在
				if (null != prizeRecordSearchVO.getPrizeId()) {
					// 添加like时需要加上%
					list.add(criteriaBuilder.equal(root.get("prizeId").as(String.class),
							prizeRecordSearchVO.getPrizeId()));
				}
				// //如果位置信息是枚举中值
				// if (EnumUtils.contains(searchPaoMaVO.getBannerPositionEnum(),
				// BannerPositionEnum.class)) {
				// list.add(criteriaBuilder.equal(root.get("bannerPositionEnum").as(String.class),
				// searchPaoMaVO.getBannerPositionEnum()));
				// }
				//
				// //如果开始时间存在
				// if (null != searchPaoMaVO.getStartTime()) {
				// list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime").as(Date.class),
				// searchPaoMaVO.getStartTime()));
				// }
				//
				// //如果结束时间存在
				// if (null != searchPaoMaVO.getEndTime()) {
				// list.add(criteriaBuilder.lessThanOrEqualTo(root.get("endTime").as(Date.class),
				// searchPaoMaVO.getEndTime()));
				// }

				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));
			}
		};
	}

}
