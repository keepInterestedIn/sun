package com.bizdata.app.informlocation.repository;

import org.springframework.data.jpa.repository.Query;

import com.bizdata.app.informlocation.domain.InformLocation;

import me.sdevil507.base.JpaBaseRepository;

public interface InformLocationRepository extends JpaBaseRepository<InformLocation, String> {
	@Query(value="select MAX(inform_id) FROM app_informlocation",nativeQuery=true)
	Integer getMaxid();

}
