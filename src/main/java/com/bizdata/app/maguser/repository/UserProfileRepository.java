package com.bizdata.app.maguser.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.bizdata.app.maguser.domain.UserProfile;

import me.sdevil507.base.JpaBaseRepository;

public interface UserProfileRepository extends JpaBaseRepository<UserProfile, String> {
	//根据accountid查找
	UserProfile findByAccountId(String id);

	List<UserProfile> findByNickNameLike(String userName);
	@Query(value="SELECT account_id FROM usr_profile WHERE nick_name LIKE ?1",nativeQuery=true)
	List<String> findAccountidByNickName(String string);

}
