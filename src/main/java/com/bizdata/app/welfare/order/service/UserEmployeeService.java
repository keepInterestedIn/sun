package com.bizdata.app.welfare.order.service;

import com.bizdata.app.welfare.order.domain.UserInfo;

public interface UserEmployeeService {

	UserInfo findOne(String userId);

}
