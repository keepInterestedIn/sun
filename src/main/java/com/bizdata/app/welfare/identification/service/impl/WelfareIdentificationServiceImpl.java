package com.bizdata.app.welfare.identification.service.impl;

import com.bizdata.app.welfare.identification.domain.EmployeeIdentification;
import com.bizdata.app.welfare.identification.repository.WelfareIdentificationRepository;
import com.bizdata.app.welfare.identification.service.WelfareIdentificationService;
import groovy.util.logging.Slf4j;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by guoshan on 2017/12/7.
 *
 * 员工认证处理
 */
@Service
@Slf4j
public class WelfareIdentificationServiceImpl implements WelfareIdentificationService {
    @Autowired
    private WelfareIdentificationRepository welfareIdentificationRepository;

    @Override
    public Page<EmployeeIdentification> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO) {
        return welfareIdentificationRepository.findAll(jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));
    }

    @Override
    @Transactional
    public boolean certifiedSuccess(Integer status, String id) {
        try {
            welfareIdentificationRepository.success(status, id);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
