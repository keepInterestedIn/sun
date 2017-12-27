package com.bizdata.app.version.service;

import com.bizdata.app.version.controller.vo.SearchVersionManagerVO;
import com.bizdata.app.version.domain.VersionManager;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;
import org.springframework.data.domain.Page;

/**
 * @author w
 */
public interface VersionManagerService {

    /**
     * 根据查询条件和分页参数获取分页后的数据
     *
     * @param jpaPageParamVO
     * @param jpaSortParamVO
     * @param searchVersionManagerVO
     * @return 分页对象
     */
    Page<VersionManager> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO, SearchVersionManagerVO searchVersionManagerVO);

    /**
     * 保存或者更新对象
     *
     * @param versionManager
     * @return 是否保存成功 false失败 true成功
     */
    boolean save(VersionManager versionManager);

    /**
     * 根据主键查询一个对象
     *
     * @param id
     * @return VersionManager 对象
     */
    VersionManager findOne(String id);

    /**
     * 根据主键删除对象
     *
     * @param id
     * @return 是否删除成功 false失败 true成功
     */
    boolean delById(String id);
}
