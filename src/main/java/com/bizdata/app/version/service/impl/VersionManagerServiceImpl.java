package com.bizdata.app.version.service.impl;

import com.bizdata.app.version.controller.vo.SearchVersionManagerVO;
import com.bizdata.app.version.domain.VersionManager;
import com.bizdata.app.version.repository.VersionManagerRepository;
import com.bizdata.app.version.service.VersionManagerService;
import lombok.extern.slf4j.Slf4j;
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
 * @author w
 */
@Service
@Slf4j
public class VersionManagerServiceImpl implements VersionManagerService {
    private final VersionManagerRepository versionManagerRepository;

    @Autowired
    public VersionManagerServiceImpl(VersionManagerRepository versionManagerRepository) {
        this.versionManagerRepository = versionManagerRepository;
    }

    @Override
    public Page<VersionManager> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO, SearchVersionManagerVO searchVersionManagerVO) {
        return versionManagerRepository.findAll(listWhereClause(searchVersionManagerVO),
                jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));
    }

    @Override
    public boolean save(VersionManager versionManager) {
        boolean flag = false;
        try {
            versionManagerRepository.save(versionManager);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("versionManager保存失败\n", e);
        }
        return flag;
    }

    @Override
    public VersionManager findOne(String id) {
        return versionManagerRepository.findOne(Integer.valueOf(id));
    }

    @Override
    public boolean delById(String id) {
        boolean flag = false;
        try {
            versionManagerRepository.delete(Integer.valueOf(id));
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("versionManager删除失败\n", e);
        }
        return flag;
    }

    private Specification<VersionManager> listWhereClause(SearchVersionManagerVO searchVersionManagerVO) {
        return new Specification<VersionManager>() {
            @Override
            public Predicate toPredicate(Root<VersionManager> root, CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();

                // 如果版本号不为空
                if (StringUtils.isNotBlank(searchVersionManagerVO.getSearchVersionId())) {
                    list.add(criteriaBuilder.equal(root.get("versionId").as(String.class), searchVersionManagerVO.getSearchVersionId()));
                }

                // 如果开始时间存在
                if (null != searchVersionManagerVO.getSearchStartTime()) {
                    list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updateTime"), searchVersionManagerVO.getSearchStartTime()));
                }
                // 如果结束时间存在
                if (null != searchVersionManagerVO.getSearchEndTime()) {
                    list.add(criteriaBuilder.lessThanOrEqualTo(root.get("updateTime"), searchVersionManagerVO.getSearchEndTime()));
                }

                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        };
    }
}
