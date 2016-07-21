/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.dao;

import java.util.List;

import com.appcel.facade.converter.enums.ConverterStatusEnum;
import com.appcel.facade.converter.enums.ConverterTypeEnum;
import com.appcel.facade.converter.mode.ConverterInfo;
import com.appcel.kernel.mybatis.dao.BaseDao;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-11
 */
public interface ConverterInfoDao extends BaseDao<ConverterInfo> {

	ConverterInfo createConverterInfo(String entityKey, String fileName, String srcFilePath, ConverterTypeEnum playMode,
			ConverterStatusEnum status, String remark);

	ConverterInfo findConverterInfoByKey(String key);

	List<ConverterInfo> findConverterInfoByEntityKey(String entityKey);

	boolean deleteConverterInfoByEntityKey(String entityKey);

	boolean deleteConverterInfoByKey(String key);

	boolean updateConverterInfoTime(String key);

	List<ConverterInfo> findConverterInfoByStatus(ConverterStatusEnum encoderSatus);

	boolean updateConverterInfoStatus(String key, ConverterStatusEnum encoderSatus);

}