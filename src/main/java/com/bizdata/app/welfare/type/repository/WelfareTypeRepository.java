package com.bizdata.app.welfare.type.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.bizdata.app.welfare.type.domain.WelfareType;

import me.sdevil507.base.JpaBaseRepository;

public interface WelfareTypeRepository extends JpaBaseRepository<WelfareType, Integer> {

	@Query(value = "select s.id, s.typeName from WelfareType as s order by s.id asc")
	List<Object> findDeviceCataNameAndId();

	@Query(value = "select typeName from WelfareType as w where w.id = ?1")
	String getTypeNameById(Integer welfareTypeId);

}
