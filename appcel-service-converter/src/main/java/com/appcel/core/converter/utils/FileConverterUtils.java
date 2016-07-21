/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.pointdew.common.utils.ContentTypeUtil;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-17
 */
public class FileConverterUtils {

	public static boolean isSupportOfficeConverted(String inputFilePath) {
		String extName = getFileSufix(inputFilePath);
		return ContentTypeUtil.isSupportedDocExt(extName);
	}

	/**
	 * 获取文件名前缀，以扩展名最后一个 . 字符开始截取，不包含 . 该字符
	 * @param fileName
	 * @return
	 */
	public static String getFilePrefix(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, splitIndex);
	}

	/**
	 * 获取文件后缀，即文件扩展名，不包含 . 字符
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileSufix(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(splitIndex + 1);
	}

	/**
	 * 拷贝文件
	 * @param inputFile 输入的源文件，即要拷贝的文件
	 * @param outputFile 输出的目标文件，即要将源文件拷贝到这个文件中
	 * 
	 * @throws FileNotFoundException 如果要拷贝的文件找不到，将抛出该异常
	 */
	public static void copyFile(String inputFile, String outputFile) throws FileNotFoundException {
		File sFile = new File(inputFile);
		File tFile = new File(outputFile);
		FileInputStream fis = new FileInputStream(sFile);
		FileOutputStream fos = new FileOutputStream(tFile);
		int temp = 0;
		try {
			while ((temp = fis.read()) != -1) {
				fos.write(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据office源文件路径获取PDF文件路径
	 * 在源文件基础上更换为指定的文件扩展名
	 * 
	 * @param inputFilePath
	 * @return
	 */
	public static String getOutputFilePathByExtName(String inputFilePath, String extName) {
		inputFilePath = inputFilePath.replaceAll("\\\\", "/");
		String outputFilePath = "";
		String temp = inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);
		outputFilePath = inputFilePath.replaceAll(temp, extName);
		return outputFilePath;
	}

	/**
	 * 根据inputFilePath文件路径和指定的fileName获取一个新的文件路径
	 * 在源文件路径基础上更换为指定的文件名。
	 * 
	 * @param inputFilePath
	 * @param fileName
	 * @return
	 */
	public static String getOutputFilePathByFileName(String inputFilePath, String fileName) {
		inputFilePath = inputFilePath.replaceAll("\\\\", "/");
		String outputFilePath = "";
		String temp = inputFilePath.substring(inputFilePath.lastIndexOf("/") + 1);
		outputFilePath = inputFilePath.replaceAll(temp, fileName);
		return outputFilePath;
	}

	public static void main(String[] args) {
		System.out.println(getOutputFilePathByExtName("F:\\Tools\\pdf2swf工具\\resource\\监测接收数据总数与高教社数据总数对比.xlsx", "jpg"));
		System.out.println(getOutputFilePathByFileName("F:\\Tools\\pdf2swf工具\\resource\\监测接收数据总数与高教社数据总数对比.xlsx", "fileName.jpg"));
	}
}
