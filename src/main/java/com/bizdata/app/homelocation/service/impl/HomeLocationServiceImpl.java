package com.bizdata.app.homelocation.service.impl;

import java.util.ArrayList;
import java.util.Date;
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

import com.bizdata.app.bpdata.domain.BpData;
import com.bizdata.app.homelocation.controller.vo.SearchHomeLocationVO;
import com.bizdata.app.homelocation.domain.HomeLocation;
import com.bizdata.app.homelocation.repository.HomeLocationRepository;
import com.bizdata.app.homelocation.service.HomeLocationService;

import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;

@Service
public class HomeLocationServiceImpl implements HomeLocationService {
	
	private final HomeLocationRepository homeLocationRepository;

    @Autowired
    public HomeLocationServiceImpl(HomeLocationRepository homeLocationRepository) {
        this.homeLocationRepository = homeLocationRepository;
    }

	@Override
	public Page<HomeLocation> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO,
			SearchHomeLocationVO searchHomeLocationVO) {
		return homeLocationRepository.findAll(listWhereClause(searchHomeLocationVO), jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));
	}

	private Specification<HomeLocation> listWhereClause(SearchHomeLocationVO searchHomeLocationVO) {
		return new Specification<HomeLocation>() {
            @Override
            public Predicate toPredicate(Root<HomeLocation> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();

                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };
	}
	@Override
	public HomeLocation findOne(String id) {
		return homeLocationRepository.findOne(id);
	}
	public boolean save(HomeLocation home) {
		boolean state;
        try {
        	homeLocationRepository.save(home);
            state = true;
        } catch (Exception e) {
            state = false;
        }
        return state;
	}

	@Override
	public Integer getMaxid() {
		return homeLocationRepository.getMaxid();
	}

	@Override
	public HomeLocation findOneByLocationid(int locationId) {
		return homeLocationRepository.findOneByLocationid(locationId);
	}

	@Override
	public HomeLocation findBylocationNo(int locationno) {
		return homeLocationRepository.findByLocationNo(locationno);
	}
}
