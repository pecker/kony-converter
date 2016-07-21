/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.manager;

import java.util.List;

import com.appcel.facade.converter.enums.ConverterStatusEnum;
import com.appcel.facade.converter.enums.ConverterTypeEnum;
import com.appcel.facade.converter.mode.ConverterInfo;
import com.appcel.kernel.mybatis.manager.BaseManager;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-12
 */
public interface ConverterInfoManager extends BaseManager<ConverterInfo> {

	ConverterInfo createConverterInfo(String entityKey, String fileName, String srcFilePath, ConverterTypeEnum converterType,
			ConverterStatusEnum status, String remark);

	ConverterInfo getConverterInfoByKey(String key);

	List<ConverterInfo> getConverterInfoByEntityKey(String entityKey);

	boolean deleteConverterInfoByEntityKey(String entityKey);

	boolean updateConverterInfoTime(String key);

	boolean deleteConverterInfoByKey(String key);

	boolean updateConverterInfoStatus(String key, ConverterStatusEnum converterSatus);

	List<ConverterInfo> getConverterInfoByStatus(ConverterStatusEnum converterSatus);

}