/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * Encoder A Java Audio/Video Encoder (based on FFMPEG)
 * 
 * Copyright (C) 2006-2014 LiRongSheng (http://www.pointdew.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.pointdew.com>.
 */
package com.appcel.core.converter.locator;

import com.appcel.core.converter.executor.ConverterExecutor;
import com.appcel.facade.converter.enums.ConverterExecutorEnum;

/**
 * 
 * Defiend class file the FFMPEGLocator.java
 * 
 * Abstract class whose derived concrete instances are used by {@link EncoderServiceImpl}
 * to locate the ffmpeg executable path.
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-2
 * @see EncoderServiceImpl
 */
public abstract class AbstractConverterLocator {

	/**
	 * This method should return the path of a EncoderExcutor executable suitable for
	 * the current machine.
	 * 
	 * @return The path of the EncoderExcutor executable.
	 */
	public abstract ConverterExecutor createConverterExecutor(ConverterExecutorEnum converterExcutorEnum);

}