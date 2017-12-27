package com.bizdata.app.bgdata.service.impl;

import com.bizdata.app.bgdata.controller.vo.SearchBgDataVO;
import com.bizdata.app.bgdata.domain.BgData;
import com.bizdata.app.bgdata.repository.BgDataRepository;
import com.bizdata.app.bgdata.service.BgDataService;
import com.bizdata.app.maguser.service.MagUserService;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author w
 *
 */
@Service
public class BgDataServiceImpl implements BgDataService {

	private final BgDataRepository bgDataRepository;

	private final MagUserService magUserService;

	@Autowired
	public BgDataServiceImpl(BgDataRepository bgDataRepository,MagUserService magUserService) {
		this.bgDataRepository = bgDataRepository;
		this.magUserService = magUserService;
	}

	@Override
	public Page<BgData> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO,
			SearchBgDataVO searchBgDataVO) {
		return bgDataRepository.findAll(listWhereClause(searchBgDataVO),
				jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));
	}

	/**
	 * 条件查询
	 *
	 * @param searchBgDataVO
	 *            条件查询VO
	 * @return 条件
	 */
	private Specification<BgData> listWhereClause(SearchBgDataVO searchBgDataVO) {
		return new Specification<BgData>() {
			@Override
			public Predicate toPredicate(Root<BgData> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<>();
				CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("profileId"));
				//如果用户ID不为空
				if (StringUtils.isNotBlank(searchBgDataVO.getProfileId())) {
					String userId = magUserService.getIdByAccountnum(Integer.valueOf(searchBgDataVO.getProfileId()));
					if(null != userId) {
						in.value(userId);
						list.add(in);
					}
				}

				// 如果设备SN号不为空
				if (StringUtils.isNotBlank(searchBgDataVO.getDeviceSn())) {
					list.add(criteriaBuilder.like(root.get("deviceSn").as(String.class),
							"%" + searchBgDataVO.getDeviceSn() + "%"));
				}

				// 如果设备编号不为空
				if (StringUtils.isNotBlank(searchBgDataVO.getDeviceNo())) {
					list.add(criteriaBuilder.like(root.get("deviceNo").as(String.class),
							"%" + searchBgDataVO.getDeviceNo() + "%"));
				}

				// 如果开始时间存在
				if (null != searchBgDataVO.getStartTime()) {
					System.out.println(searchBgDataVO.getStartTime().toGMTString());
					list.add(
							criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), searchBgDataVO.getStartTime()));
				}

				// 如果结束时间存在
				if (null != searchBgDataVO.getEndTime()) {
					list.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), searchBgDataVO.getEndTime()));
				}

				if (null != searchBgDataVO.getLevel()) {
					list.add(criteriaBuilder.lessThanOrEqualTo(root.get("level"), searchBgDataVO.getLevel()));
				}

				// 如果数据来源存在
				if (StringUtils.isNotBlank(searchBgDataVO.getDataSource())) {
					list.add(criteriaBuilder.equal(root.get("dataSource").as(String.class),
							"%"+searchBgDataVO.getDataSource()+"%"));
				}

				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));
			}
		};
	}

}
