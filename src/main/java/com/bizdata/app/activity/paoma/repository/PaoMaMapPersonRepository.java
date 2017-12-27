package com.bizdata.app.activity.paoma.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.bizdata.app.activity.paoma.domain.PaoMaMapPerson;

import me.sdevil507.base.JpaBaseRepository;

public interface PaoMaMapPersonRepository extends JpaBaseRepository<PaoMaMapPerson, String> {

	@Query(value = "select count(*) as num from activity_paoma_map_people as a where a.activity_id =?1", nativeQuery = true)
	Integer getPeopleNum(String id);

	@Query(value = "select jpushid from activity_paoma_map_people where activity_id =?1", nativeQuery = true)
	List<String> getJpushid(String n);

	@Query(value = "SELECT t1.rank FROM (SELECT (@i\\:=@i+1) AS rank, a.* FROM activity_paoma_map_people AS a,(SELECT @i\\:=0) b WHERE a.activity_id = ?1 ORDER BY a.mileage DESC) AS t1 WHERE t1.user_id = ?2", nativeQuery = true)
	public Integer getRankByActivityAndUserId(String activityId, String userId);
}
