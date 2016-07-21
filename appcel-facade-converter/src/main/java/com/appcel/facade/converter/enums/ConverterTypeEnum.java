/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.converter.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 可转换的文档类型枚举
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-10
 */
public enum ConverterTypeEnum {
	/**
	 * word文档
	 */
	WORD("word文档", 1),

	/**
	 * ppt文档
	 */
	PPT("ppt文档", 2),

	/**
	 * excel文档
	 */
	EXCEL("excel文档", 3),

	/**
	 * pdf文档
	 */
	PDF("pdf文档", 4);

	/** 名称，状态描述  */
	private String name;
	/** 枚举值 */
	private int value;

	/** 构造函数 */
	private ConverterTypeEnum(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static ConverterTypeEnum getEnum(int value) {
		ConverterTypeEnum resultEnum = null;
		ConverterTypeEnum[] enumAry = ConverterTypeEnum.values();
		for (int i = 0; i < enumAry.length; i++) {
			if (enumAry[i].getValue() == value) {
				resultEnum = enumAry[i];
				break;
			}
		}
		return resultEnum;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList() {
		ConverterTypeEnum[] ary = ConverterTypeEnum.values();
		List list = new ArrayList();
		for (int i = 0; i < ary.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", String.valueOf(ary[i].getValue()));
			map.put("desc", ary[i].getName());
			list.add(map);
		}
		return list;
	}
}
