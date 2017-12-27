package com.bizdata.app.loseweightdata.service.impl;

import com.bizdata.app.loseweightdata.controller.vo.SearchLoseWeightDataVO;
import com.bizdata.app.loseweightdata.domain.LoseWeightData;
import com.bizdata.app.loseweightdata.repository.LoseWeightDataRepository;
import com.bizdata.app.loseweightdata.service.LoseWeightDataService;
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

@Service
public class LoseWeightDataServiceImpl implements LoseWeightDataService {

    private final LoseWeightDataRepository loseWeightDataRepository;

    private final MagUserService magUserService;

    @Autowired
    public LoseWeightDataServiceImpl(LoseWeightDataRepository loseWeightDataRepository, MagUserService magUserService) {
        this.loseWeightDataRepository = loseWeightDataRepository;
        this.magUserService = magUserService;
    }

    @Override
    public Page<LoseWeightData> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO,
                                              SearchLoseWeightDataVO searchLoseWeightDataVO) {
        return loseWeightDataRepository.findAll(listWhereClause(searchLoseWeightDataVO),
                jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));
    }

    private Specification<LoseWeightData> listWhereClause(SearchLoseWeightDataVO searchLoseWeightDataVO) {
        return new Specification<LoseWeightData>() {
            @Override
            public Predicate toPredicate(Root<LoseWeightData> root, CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("profileId"));
                //如果用户ID不为空
                if (StringUtils.isNotBlank(searchLoseWeightDataVO.getProfileId())) {
                    String userId = magUserService.getIdByAccountnum(Integer.valueOf(searchLoseWeightDataVO.getProfileId()));
                    if (null != userId) {
                        in.value(userId);
                        list.add(in);
                    }
                }

                // 如果设备SN号不为空
                if (StringUtils.isNotBlank(searchLoseWeightDataVO.getDeviceSn())) {
                    list.add(criteriaBuilder.like(root.get("deviceSn").as(String.class),
                            "%" + searchLoseWeightDataVO.getDeviceSn() + "%"));
                }

                // 如果设备编号不为空
                if (StringUtils.isNotBlank(searchLoseWeightDataVO.getDeviceName())) {
                    list.add(criteriaBuilder.like(root.get("deviceName").as(String.class),
                            "%" + searchLoseWeightDataVO.getDeviceName() + "%"));
                }

                // 如果开始时间存在
                if (null != searchLoseWeightDataVO.getStartTime()) {
                    System.out.println(searchLoseWeightDataVO.getStartTime().toGMTString());
                    list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"),
                            searchLoseWeightDataVO.getStartTime()));
                }

                // 如果结束时间存在
                if (null != searchLoseWeightDataVO.getEndTime()) {
                    list.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"),
                            searchLoseWeightDataVO.getEndTime()));
                }

                // 如果体重类型存在
                if (null != searchLoseWeightDataVO.getWeightType()) {
                    list.add(criteriaBuilder.lessThanOrEqualTo(root.get("weightType"),
                            searchLoseWeightDataVO.getWeightType()));
                }

                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };
    }

}
