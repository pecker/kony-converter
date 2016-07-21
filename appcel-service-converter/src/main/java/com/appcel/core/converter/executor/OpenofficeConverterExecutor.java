/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.executor;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import com.appcel.core.converter.utils.FileConverterUtils;
import com.appcel.facade.converter.exception.ConverterException;

/**
 * 文档\PPT\excel转换为PDF实现 
 * 
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-16
 */
public class OpenofficeConverterExecutor implements ConverterExecutor {
	protected static Log LOGGER = LogFactory.getLog(SwftoolsConverterExecutor.class);
	private String openofficeHome;
	private int openofficePort;
	private OfficeManager officeManager;
	private OfficeDocumentConverter converter;
	private boolean startup = false;
	private boolean linuxed;

	public OpenofficeConverterExecutor(String openofficeHome, int port, boolean linuxed) {
		this.openofficeHome = openofficeHome;
		this.openofficePort = port;
		this.linuxed = linuxed;
	}

	public boolean isLinuxos() {
		return linuxed;
	}

	/**
	 * 启动转换服务
	 */
	public void startOpenOfficService() {

		DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();

		try {
			LOGGER.info("==========>>> 开始启动 open office service 服务...");

			configuration.setOfficeHome(openofficeHome);//设置OpenOffice.org安装目录
			configuration.setPortNumbers(openofficePort); //设置转换端口，默认为8100
			configuration.setTaskExecutionTimeout(1000 * 60 * 30L);//设置任务执行超时为5分钟
			configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);//设置任务队列超时为24小时

			officeManager = configuration.buildOfficeManager();
			officeManager.start(); //启动服务
			converter = new OfficeDocumentConverter(officeManager);

			LOGGER.info("=========>>> open office service 服务已启动.");
			startup = true;

		} catch (Exception ce) {
			ce.printStackTrace();
			LOGGER.info("open office service 服务已启动失败!详细信息:" + ce);
			stopOpenOfficService();
		}
	}

	/**
	 * 文件转换，word\ppt\excel转换到pdf
	 * 
	 * @param inputFilePath 要转换的输入源文件
	 * @param outputFilePath 转换到的输出目标文件
	 * @throws ConverterException 文件不存在或为空将抛出该异常
	 */
	public void execute(String inputFilePath, String outputFilePath) throws ConverterException {
		if (StringUtils.isBlank(inputFilePath) || StringUtils.isBlank(outputFilePath)) {
			throw new ConverterException("文档转换异常", new Throwable("inputFilePath can not is null or empty.", new Throwable(
					"outputFilePath can not is null or empty.")));
		}
		File inputFile = new File(inputFilePath);
		File outputFile = new File(outputFilePath);
		execute(inputFile, outputFile);
	}

	/**
	 * 文件转换，word\ppt\excel转换到pdf
	 * 
	 * @param inputFilePath 要转换的输入源文件
	 * @param outputFilePath 转换到的输出目标文件
	 * @throws ConverterException 文件不存在或为空将抛出该异常
	 */
	public void execute(File inputFile, File outputFile) throws ConverterException {
		if (null == inputFile || !inputFile.exists() || null == outputFile) {
			throw new ConverterException("文件不存在", new Throwable("inputFilePath or outputFile can not is null.", new Throwable(
					"inputFile文件没找到.")));
		}

		if (!FileConverterUtils.isSupportOfficeConverted(inputFile.getName())) {
			throw new ConverterException("不支持转换的文件", new Throwable("转换文件不是 office文档类文件.", new Throwable(
					"目前支持的office文档类型： (xls|xlsx|doc|docx|ppt|pptx)")));
		}
		LOGGER.info("开始进入 openoffice 文档转换为 pdf 格式... ");
		try {
			if (!outputFile.getParentFile().exists()) {
				// 假如目标路径不存在, 则新建该路径  
				outputFile.getParentFile().mkdirs();
			}
			if (!startup) {
				startOpenOfficService();
			}
			converter.convert(inputFile, outputFile);

			LOGGER.info("==========>>> openoffic 转换文档格式为 pdf 文件成功.");

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("==========>>> 转换文档到pdf失败.");
			stopOpenOfficService();
		} finally {
			stopOpenOfficService();
		}
	}

	/**
	 * 停止文档转换服务
	 */
	public void stopOpenOfficService() {
		LOGGER.info("关闭 open office service 服务....");
		if (officeManager != null) {
			converter = null;
			officeManager.stop();
			officeManager = null;
		}
		LOGGER.info("open office service 服务已关闭!");
	}
}
