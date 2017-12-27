package com.bizdata.app.device.devicemanager.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.bizdata.app.device.devicemanager.controller.vo.DeviceManagerVO;
import com.bizdata.app.device.devicemanager.domain.DeviceManager;
import com.bizdata.app.device.devicemanager.repository.DeviceManagerRepository;
import com.bizdata.app.device.devicemanager.service.DeviceManagerService;

import lombok.extern.slf4j.Slf4j;
import me.sdevil507.vo.JpaPageParamVO;
import me.sdevil507.vo.JpaSortParamVO;

@Service
@Slf4j
public class DeviceManagerServiceImpl implements DeviceManagerService {

	private final DeviceManagerRepository deviceManagerRepository;

	@Autowired
	public DeviceManagerServiceImpl(DeviceManagerRepository deviceManagerRepository) {
		this.deviceManagerRepository = deviceManagerRepository;
	}

	@Override
	public Page<DeviceManager> findAllByPage(JpaPageParamVO jpaPageParamVO, JpaSortParamVO jpaSortParamVO,
			DeviceManagerVO deviceManagerVO) {
		return deviceManagerRepository.findAll(listWhereClause(deviceManagerVO),
				jpaPageParamVO.getPageable(jpaSortParamVO.getSort()));
	}

	private Specification<DeviceManager> listWhereClause(DeviceManagerVO deviceManagerVO) {
		return new Specification<DeviceManager>() {
			@Override
			public Predicate toPredicate(Root<DeviceManager> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<>();
				// 如果设备名称存在
				if (!StringUtils.isBlank(deviceManagerVO.getDeviceName())) {
					list.add(criteriaBuilder.like(root.get("deviceName").as(String.class),
							"%" + deviceManagerVO.getDeviceName() + "%"));
				}
				// 如果设备分类主键存在
				if (!StringUtils.isBlank(deviceManagerVO.getDeviceCataId())) {
					list.add(criteriaBuilder.equal(root.get("deviceCataId").as(String.class), deviceManagerVO.getDeviceCataId()));
				}
				// 如果设备功能存在
				if (!StringUtils.isBlank(deviceManagerVO.getFunctionType())) {
					list.add(criteriaBuilder.like(root.get("functionTypeDes").as(String.class),
							"%" + deviceManagerVO.getFunctionType() + "%"));
				}
				// 如果设备状态存在
				if (!StringUtils.isBlank(deviceManagerVO.getState())) {
					list.add(criteriaBuilder.equal(root.get("state").as(String.class), deviceManagerVO.getState()));
				}
				// 如果设备连接类型存在
				if (!StringUtils.isBlank(deviceManagerVO.getConnectionType())) {
					list.add(criteriaBuilder.equal(root.get("linkType").as(String.class),
							deviceManagerVO.getConnectionType()));
				}
				// 如果开始时间和结束时间存在
				if (null != deviceManagerVO.getStartDate()) {
					list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate").as(Date.class),
							deviceManagerVO.getStartDate()));
				}
				if (null != deviceManagerVO.getEndDate()) {
					list.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate").as(Date.class),
							deviceManagerVO.getEndDate()));
				}

				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));
			}
		};
	}

	@Override
	public boolean save(DeviceManager deviceManager) {
		boolean state;
		try {
			deviceManagerRepository.save(deviceManager);
			state = true;
		} catch (Exception e) {
			log.error("新增设备管理库数据失败:", e);
			state = false;
		}
		return state;
	}

	@Override
	public DeviceManager findOne(String id) {
		return deviceManagerRepository.findOne(id);
	}

}
