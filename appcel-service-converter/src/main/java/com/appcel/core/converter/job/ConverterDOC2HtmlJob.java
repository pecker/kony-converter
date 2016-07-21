/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.appcel.core.converter.executor.ConverterExecutor;
import com.appcel.core.converter.locator.ConverterLocator;
import com.appcel.facade.converter.enums.ConverterExecutorEnum;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-17
 */
public class ConverterDOC2HtmlJob {

	/**
	 * 转换文件
	 * @param fromFileInputStream
	 * @throws FileNotFoundException 
	 * */
	public static String doc2Html(String inputFilePath, String outputFolder) throws FileNotFoundException {
		File inputFile = new File(inputFilePath);
		if (!inputFile.exists()) {
			throw new FileNotFoundException("要转换的文件不存在：" + inputFilePath);
		}
		File toFileFolder = new File(outputFolder);
		if (!toFileFolder.exists()) {
			toFileFolder.mkdirs();
		}

		InputStream fromFileInputStream = new FileInputStream(inputFile);

		//			Date date = new Date();
		//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		//			String timesuffix = sdf.format(date);

		//			String htmFileName = inputFile.getName().substring(0, inputFile.getName().lastIndexOf(".")) + ".html";
		//
		//			File htmlOutputFile = new File(toFileFolder.toString() + File.separatorChar + htmFileName);
		//			File docInputFile = new File(toFileFolder.toString() + File.separatorChar + inputFile.getName());

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String timesuffix = sdf.format(date);
		String htmFileName = "htmlfile" + timesuffix + ".html";
		String docFileName = "docfile" + timesuffix + inputFilePath.substring(inputFilePath.lastIndexOf("."));

		File htmlOutputFile = new File(toFileFolder.toString() + File.separatorChar + htmFileName);
		File docInputFile = new File(toFileFolder.toString() + File.separatorChar + docFileName);

		/**
		 * 由fromFileInputStream构建输入文件
		 * */
		try {
			OutputStream os = new FileOutputStream(docInputFile);
			int bytesRead = 0;
			byte[] buffer = new byte[1024 * 8];
			while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fromFileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		ConverterExecutor converterExecutor = ConverterLocator.getMe().createConverterExecutor(ConverterExecutorEnum.OPENOFFICE_EXECUTOR);
		converterExecutor.execute(docInputFile, htmlOutputFile);

		// 转换完之后删除word文件
		// docInputFile.delete();
		return htmFileName;
	}

	public static void main(String[] args) throws IOException {
		//Doc2HtmlUtil.doc2Html("D://pdf//转换用.ppt", "D://pdf//ttttt444//");
		//doc2Html("D://pdf//专业与院系对应关系.xls", "D://pdf//ttttt444//");
		//Doc2HtmlUtil.doc2Html("D://pdf//2010110档案管理系统需求分析说明书正式.doc", "D://pdf//ttttt444//");

		//String inputFilePath = "F:\\Tools\\pdf2swf工具\\resource/新疆虚拟仿真实训平台开发工作量评估.doc";
		//String inputFilePath = "F:\\Tools\\pdf2swf工具\\resource/sakai教学平台介绍.docx";

		String inputFilePath = "D:\\Converter\\test/EDU 2.0 for School.pptx";
		//String inputFilePath = "F:\\Tools\\pdf2swf工具\\resource/我们离SAAS有多远.ppt";

		//String inputFilePath = "F:\\Tools\\pdf2swf工具\\resource/监测接收数据总数与高教社数据总数对比.xlsx";
		//String inputFilePath = "F:\\Tools\\pdf2swf工具\\resource/上报运行数据至监测平台结果对比.xls";
		doc2Html(inputFilePath, "D:\\Converter\\test/");
	}

}
