/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.executor;

import java.io.File;

import com.appcel.facade.converter.exception.ConverterException;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-16
 */
public interface ConverterExecutor {

	boolean isLinuxos();

	void execute(String inputFilePath, String outputFilePath) throws ConverterException;

	void execute(File inputFile, File outputFile) throws ConverterException;
}
