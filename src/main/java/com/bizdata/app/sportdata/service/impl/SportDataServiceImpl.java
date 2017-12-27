package com.bizdata.app.sportdata.service.impl;

import com.bizdata.app.maguser.service.MagUserService;
import com.bizdata.app.sportdata.controller.vo.SearchSportDataVO;
import com.bizdata.app.sportdata.domain.SportData;
import com.bizdata.app.sportdata.repository.SportDataRepository;
import com.bizdata.app.sportdata.service.SportDataService;
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
 * SportDataServiceImpl
 * <p>
 * Created by sdevil507 on 2017/9/6.
 */
@Service
public class SportDataServiceImpl implements SportDataService {

    private final SportDataRepository sportDataRepository;

    private final MagUserService magUserService;

    @Autowired
    public SportDataServiceImpl(SportDataRepository sportDataRepository, MagUserService magUserService) {
        this.sportDataRepository = sportDataRepository;
        this.magUserService = magUserService;
    }

    @Override
    public Page<SportData> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO, SearchSportDataVO searchSportDataVO) {
        return sportDataRepository.findAll(listWhereClause(searchSportDataVO), jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));
    }

    @Override
    public SportData findOne(String id) {
        return sportDataRepository.findOne(id);
    }

    /**
     * 条件查询
     *
     * @param searchSportDataVO 条件查询VO
     * @return 条件
     */
    private Specification<SportData> listWhereClause(SearchSportDataVO searchSportDataVO) {
        return new Specification<SportData>() {
            @Override
            public Predicate toPredicate(Root<SportData> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("profileId"));
                //如果用户ID不为空
                if (StringUtils.isNotBlank(searchSportDataVO.getProfileId())) {
                    String userId = magUserService.getIdByAccountnum(Integer.valueOf(searchSportDataVO.getProfileId()));
                    if(null != userId) {
                        in.value(userId);
                        list.add(in);
                    }
                }

                //如果设备SN号不为空
                if (StringUtils.isNotBlank(searchSportDataVO.getDeviceSn())) {
                    list.add(criteriaBuilder.like(root.get("deviceSn").as(String.class), "%" + searchSportDataVO.getDeviceSn() + "%"));
                }

                //如果设备编号不为空
                if (StringUtils.isNotBlank(searchSportDataVO.getDeviceNo())) {
                    list.add(criteriaBuilder.like(root.get("deviceNo").as(String.class), "%" + searchSportDataVO.getDeviceNo() + "%"));
                }

                //如果开始时间存在
                if (null != searchSportDataVO.getStartTime()) {
                    list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateTime"), searchSportDataVO.getStartTime()));
                }

                //如果结束时间存在
                if (null != searchSportDataVO.getEndTime()) {
                    list.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateTime"), searchSportDataVO.getEndTime()));
                }

                //如果数据来源存在
                if (StringUtils.isNotBlank(searchSportDataVO.getDataSource())) {
                    list.add(criteriaBuilder.equal(root.get("dataSource").as(String.class),
                            "%" + searchSportDataVO.getDataSource() + "%"));
                }

                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };
    }
}
