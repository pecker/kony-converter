/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.converter.service;

import java.util.List;

import com.appcel.facade.converter.enums.ConverterStatusEnum;
import com.appcel.facade.converter.enums.ConverterTypeEnum;
import com.appcel.facade.converter.mode.ConverterInfo;

/**
 * 文档转换对外服务接口
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-12
 */
public interface ConverterInfoService {

	ConverterInfo createConverterInfo(String entityKey, String fileName, String srcFilePath, ConverterTypeEnum converterType,
			ConverterStatusEnum converterStatus, String remark);

	ConverterInfo getConverterInfoByKey(String key);

	List<ConverterInfo> getConverterInfoByEntityKey(String entityKey);

	boolean deleteConverterInfoByEntityKey(String entityKey);

	boolean updateConverterInfoTime(String key);

	boolean deleteConverterInfoByKey(String key);

}