package com.bizdata.app.maguser.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.bizdata.app.maguser.domain.UserProfile;

public interface UserProfileService {
	//根据acountid
	UserProfile findByAccountId(String id);

	Page<UserProfile> findAll(Specification<UserProfile> listWhereClause, Pageable pageable);

	List<UserProfile> findAllByNickName(String userName);
	//根据昵称获取accountid
	List<String> findAccountidByNickName(String nickName);

}
