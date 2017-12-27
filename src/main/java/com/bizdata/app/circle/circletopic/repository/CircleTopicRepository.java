package com.bizdata.app.circle.circletopic.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bizdata.app.circle.circletopic.domain.CircleTopic;

import me.sdevil507.base.JpaBaseRepository;

/**
 * 
 * @author Administrator
 *
 */
public interface CircleTopicRepository extends JpaBaseRepository<CircleTopic, String> {
	@Query(value="select max(topicid) from app_circle_topic",nativeQuery=true)
	Integer getMaxtopicid();
	//修改用户所发表全部文章的状态
	@Query(value="update app_circle_topic set state = ?2 where user_id = ?1",nativeQuery=true)
	@Modifying
	void setStateByUserid(String userid, String state);
	@Query(value="select id from app_circle_topic where topicid = ?1",nativeQuery=true)
	String getIdBytopicid(int id);
	@Modifying
	@Transactional
	@Query(value="update app_circle_topic set state = ?2 where class_id = ?1",nativeQuery=true)
	void setStateByClassId(String classid, String state);
	@Modifying
	@Transactional
	@Query(value="update app_circle_topic set state = ?2 where circleeeid = ?1",nativeQuery=true)
	void setStateByCircleId(String circleid, String state);
}
