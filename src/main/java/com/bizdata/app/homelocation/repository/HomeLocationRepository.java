package com.bizdata.app.homelocation.repository;

import org.springframework.data.jpa.repository.Query;

import com.bizdata.app.homelocation.domain.HomeLocation;

import me.sdevil507.base.JpaBaseRepository;

public interface HomeLocationRepository extends JpaBaseRepository<HomeLocation, String> {
	@Query(value="select MAX(location_no) FROM app_homelocation",nativeQuery=true)
	Integer getMaxid();
	@Query(value="select * FROM app_homelocation where location_id = ?1",nativeQuery=true)
	HomeLocation findOneByLocationid(int locationId);
	@Query(value="select * from app_homelocation where location_no = ?1",nativeQuery=true)
	HomeLocation findByLocationNo(int locationno);

}
