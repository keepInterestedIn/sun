package com.bizdata.app.banner.service.impl;

import com.bizdata.app.banner.constant.BannerPositionEnum;
import com.bizdata.app.banner.constant.GotoChildEnum;
import com.bizdata.app.banner.controller.vo.BannerSearchVO;
import com.bizdata.app.banner.domain.Banner;
import com.bizdata.app.banner.repository.BannerRepository;
import com.bizdata.app.banner.service.BannerService;
import com.bizdata.app.circle.circle_mag.service.CircleService;
import com.bizdata.app.circle.circletopic.service.CircleTopicService;
import com.bizdata.app.content.article.service.ArticleService;

import lombok.extern.slf4j.Slf4j;
import me.sdevil507.enum_util.EnumUtils;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BannerServiceImpl
 * <p>
 * Created by sdevil507 on 2017/9/6.
 */
@Service
@Slf4j
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    @Autowired
    private CircleTopicService circleTopicService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    public BannerServiceImpl(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    @Override
    public boolean save(Banner banner) {
        boolean state;
        //设置跳转id
    	int s = 0;
    	try{
    		s=Integer.parseInt(banner.getGotonum());
    	}catch (Exception e) {
    		e.printStackTrace();
    		s=0;
    	}
    	if(banner.getGotoChildEnum()==GotoChildEnum.ARTICLE){
    		banner.setGotoid(articleService.getIdByArticleid(s));
    	}else if(banner.getGotoChildEnum()==GotoChildEnum.CIRCLE){
    		banner.setGotoid(banner.getGotonum());
    	}else if(banner.getGotoChildEnum()==GotoChildEnum.TOPIC){
    		banner.setGotoid(circleTopicService.getIdBytopicid(s));
    	}
    	//保存
        try {
            bannerRepository.save(banner);
            state = true;
        } catch (Exception e) {
            log.error("保存或更新Banner失败:", e);
            state = false;
        }
        return state;
    }

    @Override
    public Page<Banner> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO, BannerSearchVO bannerSearchVO) {
        return bannerRepository.findAll(listWhereClause(bannerSearchVO), jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));
    }

    @Override
    public boolean delete(String id) {
        boolean state;
        try {
            bannerRepository.delete(id);
            state = true;
        } catch (Exception e) {
            log.error("Banner删除失败:", e);
            state = false;
        }
        return state;
    }

    @Override
    public Banner findOne(String id) {
        return bannerRepository.findOne(id);
    }

    /**
     * 条件查询
     *
     * @param bannerSearchVO 条件查询VO
     * @return 条件
     */
    private Specification<Banner> listWhereClause(BannerSearchVO bannerSearchVO) {
        return new Specification<Banner>() {
            @Override
            public Predicate toPredicate(Root<Banner> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //如果位置信息是枚举中值
                if (EnumUtils.contains(bannerSearchVO.getBannerPositionEnum(), BannerPositionEnum.class)) {
                    list.add(criteriaBuilder.equal(root.get("bannerPositionEnum").as(String.class), bannerSearchVO.getBannerPositionEnum()));
                }

                //如果开始时间存在
                if (null != bannerSearchVO.getStartTime()) {
                    list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime").as(Date.class), bannerSearchVO.getStartTime()));
                }

                //如果结束时间存在
                if (null != bannerSearchVO.getEndTime()) {
                    list.add(criteriaBuilder.lessThanOrEqualTo(root.get("endTime").as(Date.class), bannerSearchVO.getEndTime()));
                }

                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };
    }
}
