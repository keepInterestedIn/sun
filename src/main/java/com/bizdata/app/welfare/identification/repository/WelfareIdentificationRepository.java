package com.bizdata.app.welfare.identification.repository;

import com.bizdata.app.welfare.identification.domain.EmployeeIdentification;
import me.sdevil507.base.JpaBaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by guoshan on 2017/12/7.
 */
public interface WelfareIdentificationRepository extends JpaBaseRepository<EmployeeIdentification, String> {
    @Query(value="update employee_identification set status = ?1 where id = ?2",nativeQuery=true)
    @Modifying
    void success(Integer status, String id);
}
