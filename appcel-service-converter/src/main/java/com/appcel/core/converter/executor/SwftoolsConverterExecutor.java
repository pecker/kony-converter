/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.appcel.core.converter.utils.FileConverterUtils;
import com.appcel.facade.converter.exception.ConverterException;

/**
 * PDF文件转换为SWF文件实现
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-16
 */
public class SwftoolsConverterExecutor implements ConverterExecutor {

	protected static Log LOGGER = LogFactory.getLog(SwftoolsConverterExecutor.class);

	private String swftoolsHome;
	private String swftoolsFontHome;

	private String swftoolsExecutor;

	private boolean linuxed;

	public SwftoolsConverterExecutor(String swftoolsHome, String swftoolsFontHome, String swftoolsExecutor, boolean linuxed) {
		this.swftoolsHome = swftoolsHome;
		this.swftoolsFontHome = swftoolsFontHome;
		this.swftoolsExecutor = swftoolsExecutor;
		this.linuxed = linuxed;
	}

	public boolean isLinuxos() {
		return linuxed;
	}

	/**
	 * 支持pdf格式转换到swf
	 * 
	 * @param srcPdfFile
	 * @param distSwfFile
	 * @throws Exception
	 */
	public void execute(File srcPdfFile, File distSwfFile) throws ConverterException {
		String srcPdfFilePath = srcPdfFile.getAbsolutePath();
		if (!srcPdfFilePath.endsWith(".pdf")) {
			LOGGER.info("==========>>>文件格式非PDF！");
			throw new ConverterException("==========>>>文件格式非PDF");
		}
		if (!srcPdfFile.exists()) {
			LOGGER.info("PDF文件不存在");
			throw new ConverterException("==========>>>PDF文件不存在");
		}
		if (distSwfFile.exists()) {
			LOGGER.info("==========>>> SWF文件已存在");
			throw new ConverterException("==========>>> SWF文件已存在");
		}

		/*String command = pdf2swfSetupHome + "\\pdf2swf.exe" + " \"" + srcPdfFileAbsolutePath + "\" -o " + distSwfFileAbsolutePath
		+ " -T 9 -f";
		return command;*/

		StringBuffer cmd = new StringBuffer();
		cmd.append(swftoolsHome);
		cmd.append("\\");
		cmd.append(swftoolsExecutor + " -s languagedir=");
		cmd.append(swftoolsFontHome);
		cmd.append(" \"");
		cmd.append(srcPdfFile.getAbsolutePath());
		cmd.append("\" -o \"");
		cmd.append(distSwfFile.getAbsolutePath());
		cmd.append("\" -T 9 -f\"");

		LOGGER.info("swf tools execute command==========>>>" + cmd.toString());
		executeConvert(cmd.toString());
	}

	/**
	 * 支持pdf格式转换到swf
	 * 
	 * @param srcPdfFilePath 为绝对路径 src absolute path
	 * @param distSwfFilePath 为绝对路径 dist absolute path
	 * @throws Exception
	 */
	public void execute(String srcPdfFilePath, String distSwfFilePath) throws ConverterException {
		File srcPdfFile = new File(srcPdfFilePath);
		File distSwfFile = new File(distSwfFilePath);
		execute(srcPdfFile, distSwfFile);
	}

	/**
	 * 支持pdf格式转换到swf
	 * 
	 * @param srcPdfFilePath 源文件：要转换的pdf文件路径，为绝对路径Absolute path
	 * @throws Exception
	 * @param srcPdfFilePath
	 * @throws Exception
	 */
	public void convert2Swf(String srcPdfFilePath) throws ConverterException {
		String distSwfFilePath = FileConverterUtils.getFilePrefix(srcPdfFilePath) + ".swf";
		execute(srcPdfFilePath, distSwfFilePath);
	}

	/**
	* converter 2.2.2版本转换 pdf2swf
	* 
	* # pdf-chinese-simplified 中文字体文件 gkai00mp.ttf
	* # 主要解决SWFTools转换PDF到SWF时中文乱码问题
	* # 1.部署 xpdf-chinese-simplified到具体的目录下
	* # 2.将字体（gkai00mp.ttf）文件放到xpdf-chinese-simplified/CMap目录下
	* # 3.修改xpdf-chinese-simplified目录下的add-to-xpdfrc文件。将里面的路径设为自己的路径，并将fontFileCC的注释#去掉。
	* # 4.在调用pdf2swf命令中加入" -s languagedir=D:\\xpdf\\xpdf-chinese-simplified "参数
	* 
	* @param srcPdfFile 源文件：pdf文件
	* @param distSwfFile 目标文件：flash文件
	* @throws Exception
	*/
	public void convert222Version2Swf(File srcPdfFile, File distSwfFile) throws Exception {
		StringBuffer cmd = new StringBuffer();
		cmd.append("\"");
		cmd.append(swftoolsHome);
		cmd.append("/");
		cmd.append(swftoolsExecutor);//"pdf2swf.exe\
		cmd.append(" -z -q -s languagedir=");
		cmd.append(swftoolsFontHome);
		cmd.append(" flashversion=9 \"");
		cmd.append(srcPdfFile.getAbsolutePath());
		cmd.append("\" -o \"");
		cmd.append(distSwfFile.getAbsolutePath());
		cmd.append("\"");
		executeConvert(cmd.toString());
	}

	private int executeConvert(String command) throws ConverterException {
		LOGGER.info("开始将 pdf 文档转换编码为 swf 格式... ");
		int exitValue = -1;
		try {

			String[] envp = new String[1];
			envp[0] = "PATH=" + swftoolsHome;
			Process process = Runtime.getRuntime().exec(command, envp);

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while (bufferedReader.readLine() != null) {
				String text = bufferedReader.readLine();
				LOGGER.info("==========>>>" + text);
			}

			int waitfor = process.waitFor();
			if (waitfor != 0) {
				//p.exitValue()==0表示正常结束，1：非正常结束
				if (process.exitValue() == 1) {
					LOGGER.info("==========>>> swftools 转换 Failed!");
				}
			}
			LOGGER.info("==========>>> swftools 转换为 swf 文件成功.");
		} catch (InterruptedException e) {
			e.printStackTrace();
			LOGGER.error("==========>>> swftools 转换为 swf 文件失败." + "Message : " + e.getMessage() + "Cause : " + e.getCause());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("==========>>> swftools 转换为 swf 文件失败." + "Message : " + e.getMessage() + "Cause : " + e.getCause());
		}

		return exitValue;
	}

	/*
	 * 进行测试转换是否成功
	 * 
	 */
	public static void main(String[] args) {
		//String inputFilePath = "F:\\Tools\\pdf2swf工具\\resource/新疆虚拟仿真实训平台开发工作量评估.doc";
		//String inputFilePath = "F:\\Tools\\pdf2swf工具\\resource/sakai教学平台介绍.docx";

		//String inputFilePath = "F:\\Tools\\pdf2swf工具\\resource/EDU 2.0 for School.pptx";
		//String inputFilePath = "F:\\Tools\\pdf2swf工具\\resource/我们离SAAS有多远.ppt";

		//String inputFilePath = "F:\\Tools\\pdf2swf工具\\resource/监测接收数据总数与高教社数据总数对比.xlsx";
		//String inputFilePath = "F:\\Tools\\pdf2swf工具\\resource/上报运行数据至监测平台结果对比.xls";
		//String outputFilePath = getOutputFilePath(inputFilePath);
		//Office转换成Pdf
		//OfficeToPdf.office2pdf(inputFilePath, outputFilePath);

		//addFooterAndWater(outputFilePath, "F:\\Tools\\pdf2swf工具\\resource/新疆虚拟仿真实训平台开发工作量评估3.pdf", "水印严禁复制", "kony@iRockey页眉",
		//		"kony@iRockey页脚");
		//添加水印、页眉、页脚
		//addFooterAndWater("F:/Tools/pdf2swf工具/resource/中国在线教育发展的历程与现状.pdf", "F:\\Tools\\pdf2swf工具\\resource/中国在线教育发展的历程与现状3.pdf",
		//		"WordToPdf水印严禁复制", "WordToPdf页眉", "WordToPdf页脚");

	}
}
