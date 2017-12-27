package com.bizdata.app.imagecode;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import me.sdevil507.base.JpaBaseRepository;

public interface ImageCodeRepository extends JpaBaseRepository<ImageCode, String> {
	@Query(value="select * from app_imagecode where code = ?1 and sessionid = ?4 and timestamp >= ?2 and timestamp <= ?3",nativeQuery=true)
	ImageCode findOne(String code, long l, long s,String sessionid);
	@Modifying
	@Transactional
	@Query(value="delete from app_imagecode where sessionid = ?1",nativeQuery=true)
	void deleteBySessionid(String sessionid);
}
