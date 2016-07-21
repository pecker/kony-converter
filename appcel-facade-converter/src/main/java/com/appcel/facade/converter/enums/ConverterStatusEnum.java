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
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-10
 */
public enum ConverterStatusEnum {
	/**
	 * 待上传转换文件
	 */
	STATUS_WAIT_UPLOAD("待上传", 1),

	/**
	 * 待转PDF文件
	 */
	STATUS_WAIT_PDF("待转PDF", 2),
	/**
	 * 待加水印文件
	 */
	STATUS_WAIT_WATERMARK("待加水印", 3),
	/**
	 * 待转截图文件 CaptureImage
	 */
	STATUS_WAIT_CAPTUREIMG("待截图", 4),

	/**
	 * 待转WORD、PPT、EXCEL，转换为swf
	 */
	STATUS_WAIT_CONVERT("待转换", 5),

	/**
	 * 已转换文件，转换已完成
	 */
	STATUS_COMPLETE("已转换", 6),

	/**
	 * 转换失败
	 */
	STATUS_FAIL("转换失败", 7);

	/** 名称，状态描述  */
	private String name;
	/** 枚举值 */
	private int value;

	/** 构造函数 */
	private ConverterStatusEnum(String name, int value) {
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

	public static ConverterStatusEnum getEnum(int value) {
		ConverterStatusEnum resultEnum = null;
		ConverterStatusEnum[] enumAry = ConverterStatusEnum.values();
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
		ConverterStatusEnum[] ary = ConverterStatusEnum.values();
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
