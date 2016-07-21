/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.converter.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appcel.core.converter.manager.ConverterInfoManager;
import com.appcel.facade.converter.enums.ConverterStatusEnum;
import com.appcel.facade.converter.enums.ConverterTypeEnum;
import com.appcel.facade.converter.mode.ConverterInfo;
import com.appcel.facade.converter.service.ConverterInfoService;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-12
 */
@Service("converterInfoService")
public class ConverterInfoServiceImpl implements ConverterInfoService {

	@Autowired
	ConverterInfoManager converterInfoManager;

	public ConverterInfo createConverterInfo(String entityKey, String fileName, String srcFilePath, ConverterTypeEnum converterType,
			ConverterStatusEnum status, String remark) {
		return converterInfoManager.createConverterInfo(entityKey, fileName, srcFilePath, converterType, status, remark);
	}

	public ConverterInfo getConverterInfoByKey(String key) {
		return converterInfoManager.getConverterInfoByKey(key);
	}

	public List<ConverterInfo> getConverterInfoByEntityKey(String entityKey) {
		return converterInfoManager.getConverterInfoByEntityKey(entityKey);
	}

	public boolean deleteConverterInfoByEntityKey(String entityKey) {
		return converterInfoManager.deleteConverterInfoByEntityKey(entityKey);
	}

	public boolean updateConverterInfoTime(String key) {
		return converterInfoManager.updateConverterInfoTime(key);
	}

	public boolean deleteConverterInfoByKey(String key) {
		return converterInfoManager.deleteConverterInfoByKey(key);
	}

}
