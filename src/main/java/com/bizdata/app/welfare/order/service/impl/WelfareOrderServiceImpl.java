package com.bizdata.app.welfare.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import com.bizdata.app.maguser.service.MagUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bizdata.app.maguser.domain.UserProfile;
import com.bizdata.app.maguser.service.UserProfileService;
import com.bizdata.app.welfare.order.controller.vo.SearchWelfareOrderVO;
import com.bizdata.app.welfare.order.domain.WelfareOrder;
import com.bizdata.app.welfare.order.repository.WelfareOrderRepository;
import com.bizdata.app.welfare.order.service.WelfareOrderService;

import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;

/**
 * @author w
 */
@Service
public class WelfareOrderServiceImpl implements WelfareOrderService {
    @Autowired
    private WelfareOrderRepository welfareOrderRepository;
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private MagUserService magUserService;


    @Override
    public Page<WelfareOrder> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO,
                                            SearchWelfareOrderVO searchWelfareOrderVO) {
        return welfareOrderRepository.findAll(listWhereClause(searchWelfareOrderVO),
                jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));
    }

    private Specification<WelfareOrder> listWhereClause(SearchWelfareOrderVO searchWelfareOrderVO) {
        return new Specification<WelfareOrder>() {
            @Override
            public Predicate toPredicate(Root<WelfareOrder> root, CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                // 用于存放另外几个表的查询结果
                In<String> in = criteriaBuilder.in(root.get("userId"));
                // 临时存放in变量
                List<String> tempIn = new ArrayList<String>();
                // 如果姓名不为空
                if (StringUtils.isNotBlank(searchWelfareOrderVO.getRealName())) {
                    list.add(criteriaBuilder.equal(root.get("realName").as(String.class),
                            searchWelfareOrderVO.getRealName()));
                }
                //如果昵称不为空
                if (StringUtils.isNotBlank(searchWelfareOrderVO.getNickName())) {
                    List<String> accountIds = userProfileService.findAccountidByNickName(searchWelfareOrderVO.getNickName());

                    if (null != accountIds && accountIds.size() > 0) {
                        for (String temp : accountIds) {
                            if (null != temp && !"".equals(temp)) {
                                tempIn.add(temp);
                            }
                        }
                    }
                }
                // 如果手机号不为空
                if (StringUtils.isNotBlank(searchWelfareOrderVO.getMobile())) {
                    List<String> accountIds = magUserService.findIdByMobile(searchWelfareOrderVO.getMobile());

                    if (null != accountIds && accountIds.size() > 0) {
                        for (String temp : accountIds) {
                            if (null != temp && !"".equals(temp)) {
                                tempIn.add(temp);
                            }
                        }
                    }
                }
                // 如果tempIn中不为空则可以添加到条件中去
                if (tempIn.size() > 0) {
                    for (String temp : tempIn) {
                        in.value(temp);
                    }
                    list.add(in);
                }
                // 如果证件类型不为空
                if (StringUtils.isNotBlank(searchWelfareOrderVO.getIdType())) {
                    list.add(criteriaBuilder.equal(root.get("idType").as(String.class),
                            searchWelfareOrderVO.getIdType()));
                }
                // 如果证件号码不为空
                if (StringUtils.isNotBlank(searchWelfareOrderVO.getIdNum())) {
                    list.add(
                            criteriaBuilder.equal(root.get("idNum").as(String.class), searchWelfareOrderVO.getIdNum()));
                }
                // 如果活动id不为空
                if (StringUtils.isNotBlank(searchWelfareOrderVO.getWelfareId())) {
                    list.add(criteriaBuilder.equal(root.get("welfareId").as(String.class),
                            searchWelfareOrderVO.getWelfareId()));
                }
                // 如果城市不为空
                if (StringUtils.isNotBlank(searchWelfareOrderVO.getCity())) {
                    list.add(criteriaBuilder.equal(root.get("city").as(String.class), searchWelfareOrderVO.getCity()));
                }
                // 如果科室不为空
                if (StringUtils.isNotBlank(searchWelfareOrderVO.getDepartments())) {
                    list.add(criteriaBuilder.equal(root.get("departments").as(String.class),
                            searchWelfareOrderVO.getDepartments()));
                }
                // 如果医院不为空
                if (StringUtils.isNotBlank(searchWelfareOrderVO.getHospital())) {
                    list.add(criteriaBuilder.equal(root.get("hospital").as(String.class),
                            searchWelfareOrderVO.getHospital()));
                }
                // 如果福利类型不为空
                if (StringUtils.isNotBlank(searchWelfareOrderVO.getWelfareType())) {
                    list.add(criteriaBuilder.equal(root.get("welfareType").as(String.class),
                            searchWelfareOrderVO.getWelfareType()));
                }
                // 如果开始时间存在
                if (null != searchWelfareOrderVO.getStartDate()) {
                    list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("seeDoctorDate").as(Date.class),
                            searchWelfareOrderVO.getStartDate()));
                }
                // 如果结束时间存在
                if (null != searchWelfareOrderVO.getEndDate()) {
                    list.add(criteriaBuilder.lessThanOrEqualTo(root.get("seeDoctorDate").as(Date.class),
                            searchWelfareOrderVO.getEndDate()));
                }

                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };
    }

    @Override
    public List<WelfareOrder> findAll() {
        return welfareOrderRepository.findAll();
    }

    @Override
    public Integer getPeopleByWelfare(String id) {
        return welfareOrderRepository.getPeopleByWelfare(id);
    }

    @Override
    public List<WelfareOrder> findAll(SearchWelfareOrderVO searchWelfareOrderVO) {
        return welfareOrderRepository.findAll(listWhereClause(searchWelfareOrderVO));
    }
}
