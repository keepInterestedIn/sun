package com.bizdata.app.maguser.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bizdata.app.maguser.domain.UserProfile;
import com.bizdata.app.maguser.repository.UserProfileRepository;
import com.bizdata.app.maguser.service.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {

	private UserProfileRepository userProfileRepository;

	@Autowired
	public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
		this.userProfileRepository = userProfileRepository;
	}

	/*
	 * 根据accountId查找
	 * 
	 * @see
	 * com.bizdata.app.maguser.service.UserProfileService#findByAccountId(java.lang.
	 * String)
	 */
	@Override
	public UserProfile findByAccountId(String id) {
		return userProfileRepository.findByAccountId(id);
	}

	@Override
	public Page<UserProfile> findAll(Specification<UserProfile> listWhereClause, Pageable pageable) {
		return userProfileRepository.findAll(listWhereClause, pageable);
	}

	@Override
	public List<UserProfile> findAllByNickName(String userName) {
		return userProfileRepository.findByNickNameLike("%" + userName + "%");
	}

	@Override
	public List<String> findAccountidByNickName(String nickName) {
		return userProfileRepository.findAccountidByNickName("%" + nickName + "%");
	}

}
