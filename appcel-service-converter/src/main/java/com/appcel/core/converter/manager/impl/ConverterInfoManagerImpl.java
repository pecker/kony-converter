/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appcel.core.converter.dao.ConverterInfoDao;
import com.appcel.core.converter.manager.ConverterInfoManager;
import com.appcel.facade.converter.enums.ConverterStatusEnum;
import com.appcel.facade.converter.enums.ConverterTypeEnum;
import com.appcel.facade.converter.mode.ConverterInfo;
import com.appcel.kernel.mybatis.dao.BaseDao;
import com.appcel.kernel.mybatis.manager.BaseManagerImpl;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-12
 */
@Service("converterInfoManager")
public class ConverterInfoManagerImpl extends BaseManagerImpl<ConverterInfo> implements ConverterInfoManager {

	@Autowired
	ConverterInfoDao converterInfoDao;

	public ConverterInfo createConverterInfo(String entityKey, String fileName, String srcFilePath, ConverterTypeEnum converterType,
			ConverterStatusEnum status, String remark) {
		return converterInfoDao.createConverterInfo(entityKey, fileName, srcFilePath, converterType, status, remark);
	}

	public ConverterInfo getConverterInfoByKey(String key) {
		return converterInfoDao.findConverterInfoByKey(key);
	}

	public List<ConverterInfo> getConverterInfoByEntityKey(String entityKey) {
		return converterInfoDao.findConverterInfoByEntityKey(entityKey);
	}

	public List<ConverterInfo> getConverterInfoByStatus(ConverterStatusEnum encoderSatus) {
		return converterInfoDao.findConverterInfoByStatus(encoderSatus);
	}

	public boolean deleteConverterInfoByEntityKey(String entityKey) {
		return converterInfoDao.deleteConverterInfoByEntityKey(entityKey);
	}

	public boolean updateConverterInfoTime(String key) {
		return converterInfoDao.updateConverterInfoTime(key);
	}

	public boolean updateConverterInfoStatus(String key, ConverterStatusEnum encoderSatus) {
		return converterInfoDao.updateConverterInfoStatus(key, encoderSatus);
	}

	public boolean deleteConverterInfoByKey(String key) {
		return converterInfoDao.deleteConverterInfoByKey(key);
	}

	@Override
	protected BaseDao<ConverterInfo> getDao() {

		return this.converterInfoDao;
	}

}
