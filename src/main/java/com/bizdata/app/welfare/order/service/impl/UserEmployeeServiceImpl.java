package com.bizdata.app.welfare.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizdata.app.welfare.order.domain.UserInfo;
import com.bizdata.app.welfare.order.repository.UserEmployeeRepository;
import com.bizdata.app.welfare.order.service.UserEmployeeService;

@Service
public class UserEmployeeServiceImpl implements UserEmployeeService {
	@Autowired
	private UserEmployeeRepository userEmployeeRepository;

	@Override
	public UserInfo findOne(String userId) {
		return userEmployeeRepository.findOne(userId);
	}
	
}
