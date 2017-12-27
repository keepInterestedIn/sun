package com.bizdata.app.welfare.firm.controller.vo;

import java.util.Map;

import com.bizdata.app.welfare.firm.domain.WelfareFirm;

import lombok.Data;

@Data
public class WelfareFirmVO {
	private Map<Integer, String> welfareTypes;

	private WelfareFirm welfareFirm;
}
