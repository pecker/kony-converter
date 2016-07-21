/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.appcel.core.converter.dao.ConverterInfoDao;
import com.appcel.facade.converter.enums.ConverterStatusEnum;
import com.appcel.facade.converter.enums.ConverterTypeEnum;
import com.appcel.facade.converter.mode.ConverterInfo;
import com.appcel.kernel.mybatis.dao.BaseDaoImpl;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-11
 */
@Repository("converterInfoDao")
public class ConverterInfoDaoImpl extends BaseDaoImpl<ConverterInfo> implements ConverterInfoDao {

	public ConverterInfo createConverterInfo(String entityKey, String fileName, String srcFilePath, ConverterTypeEnum converterType,
			ConverterStatusEnum status, String remark) {
		ConverterInfo encoderInfo = new ConverterInfo(entityKey, fileName, srcFilePath, converterType.getValue(), status.getValue(), remark);
		String key = save(encoderInfo);
		encoderInfo.setKey(key);
		return encoderInfo;
	}

	public ConverterInfo findConverterInfoByKey(String key) {
		ConverterInfo encoderInfo = findByKey(key);
		return encoderInfo;
	}

	public List<ConverterInfo> findConverterInfoByEntityKey(String entityKey) {
		return super.getSessionTemplate().selectList("listByEntityKey", entityKey);
	}

	public List<ConverterInfo> findConverterInfoByStatus(ConverterStatusEnum converterSatus) {
		Integer status = converterSatus.getValue();
		return super.getSessionTemplate().selectList("listByStatus", status);
	}

	public boolean deleteConverterInfoByEntityKey(String entityKey) {
		List<ConverterInfo> encoderInfos = findConverterInfoByEntityKey(entityKey);
		if (null != encoderInfos && !encoderInfos.isEmpty()) {
			for (ConverterInfo encoderInfo : encoderInfos) {
				deleteByKey(encoderInfo.getKey());
			}
			return true;
		}
		return false;
	}

	public boolean updateConverterInfoTime(String key) {
		ConverterInfo encoderInfo = findByKey(key);
		if (encoderInfo != null) {
			encoderInfo.setConverterTime(new Date());
			update(encoderInfo);
			return true;
		}
		return false;
	}

	public boolean updateConverterInfoStatus(String key, ConverterStatusEnum converterSatus) {
		ConverterInfo encoderInfo = findByKey(key);
		if (encoderInfo != null) {
			encoderInfo.setStatus(converterSatus.getValue());
			update(encoderInfo);
			return true;
		}
		return false;
	}

	public boolean deleteConverterInfoByKey(String key) {
		ConverterInfo encoderInfo = findByKey(key);
		if (encoderInfo != null) {
			deleteByKey(encoderInfo.getKey());
			return true;
		}
		return false;
	}
}
