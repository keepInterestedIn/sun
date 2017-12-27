package com.bizdata.app.welfare.identification.service;

import com.bizdata.app.welfare.identification.domain.EmployeeIdentification;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;
import org.springframework.data.domain.Page;

/**
 * Created by guoshan on 2017/12/7.
 *
 * 员工认证处理
 */
public interface WelfareIdentificationService {
    Page<EmployeeIdentification> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO);

    boolean certifiedSuccess(Integer status, String id);
}
