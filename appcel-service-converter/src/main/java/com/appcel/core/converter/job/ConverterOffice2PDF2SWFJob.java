/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.job;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.appcel.core.converter.FastDFSClient;
import com.appcel.core.converter.config.ConverterConfiguration;
import com.appcel.core.converter.executor.CaptureImageConverterExecutor;
import com.appcel.core.converter.executor.ConverterExecutor;
import com.appcel.core.converter.executor.WatermarkConverterExecutor;
import com.appcel.core.converter.locator.ConverterLocator;
import com.appcel.core.converter.manager.ConverterInfoManager;
import com.appcel.core.converter.utils.FileConverterUtils;
import com.appcel.facade.converter.enums.ConverterExecutorEnum;
import com.appcel.facade.converter.enums.ConverterStatusEnum;
import com.appcel.facade.converter.mode.ConverterInfo;

/**
 * 转换office套件（word\excel\ppt）到pdf到swf调度工作器
 * 
 * 在转换为pdf后，对文档添加水印、页眉、页脚、logo，并进行截图。 *  
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-17
 */
public class ConverterOffice2PDF2SWFJob {
	private static Log LOGGER = LogFactory.getLog(ConverterOffice2PDF2SWFJob.class);

	private final static String OFFICEPATH = "office";
	private final static String LOGOPATH = "logo";
	private final static String WATERMARKPATH = "watermark";

	@Autowired
	ConverterInfoManager converterInfoManager;

	boolean linuxed;
	boolean retained = ConverterConfiguration.getMe().getConverterFileRetained();

	public ConverterOffice2PDF2SWFJob() {

	}

	public void doConvert() {

		// 查询出全部需要待截图的转换文件
		List<ConverterInfo> convertInfos = converterInfoManager.getConverterInfoByStatus(ConverterStatusEnum.STATUS_WAIT_PDF);

		if (convertInfos != null && !convertInfos.isEmpty()) {

			//----------因为文件系统分布式部署的原因，需要有一个专门做转换的场所，从配置中获取临时转换路径
			String tempFilePath = ConverterConfiguration.getMe().getConverterExecuteDir();

			//----------构造转换文件的目录
			StringBuffer realfileDir = new StringBuffer(tempFilePath);
			realfileDir.append("/");
			realfileDir.append(OFFICEPATH);
			realfileDir.append("/");

			//----------检查目录文件是否存在
			File realfileDirFile = new File(realfileDir.toString());
			if (!realfileDirFile.exists()) {
				LOGGER.info("Directory not exist. Create it.");
				LOGGER.info(realfileDirFile);
				realfileDirFile.mkdirs();
			}

			for (ConverterInfo converterInfo : convertInfos) {

				try {
					LOGGER.info("开始处理文件转换 =============>>> " + converterInfo);

					StringBuffer realfileoriPath = new StringBuffer(realfileDir);

					//----------上传后原始媒体文件存放路径
					String docFilePath = converterInfo.getSrcFilePath();

					//----------获取要转换的原始文件的文件名
					String srcFileName = docFilePath.substring(docFilePath.lastIndexOf("/") + 1, docFilePath.length());

					//---------构造要下载的文件路径和文件名，也是等待要转换的文件路径
					String fileNamePath = srcFileName.substring(0, srcFileName.lastIndexOf("."));
					realfileoriPath.append(fileNamePath);
					realfileoriPath.append("/");
					File encoderFileDir = new File(realfileoriPath.toString());
					if (!encoderFileDir.exists()) {
						LOGGER.info("==========>>> Directory not exist. Create it.");
						LOGGER.info("==========>>>" + encoderFileDir);
						encoderFileDir.mkdirs();
					}

					realfileoriPath.append(srcFileName);
					//---------从文件系统中下载要转换的源文件，也是等待要转换的文件
					File sourceFile = new File(realfileoriPath.toString());
					if (!sourceFile.exists()) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("转换源文件下载路径==========>>> " + realfileoriPath);
						}
						InputStream inputStream = FastDFSClient.downloadFile(docFilePath);
						FileUtils.copyInputStreamToFile(inputStream, sourceFile);
						LOGGER.info("==========>>> 转换源文件已下载");
					}

					//----------调用执行转换器执行截图转换命令
					executeJob(converterInfo, realfileoriPath.toString());

					//---------更新转换状态为 4，已完成
					converterInfo.setStatus(ConverterStatusEnum.STATUS_COMPLETE.getValue());

					//----------更新转换信息数据
					converterInfo.setConverterTime(new Date());
					converterInfoManager.update(converterInfo);

					LOGGER.info("处理文件转换已完成，转换数据已更新 =============>>> " + converterInfo);

					//Rest--------------------------
					Thread.sleep(10 * 1000);

				} catch (Exception e) {
					converterInfoManager.updateConverterInfoStatus(converterInfo.getKey(), ConverterStatusEnum.STATUS_FAIL);
					LOGGER.info("处理文件转换失败 =============>>> " + converterInfo);
					e.printStackTrace();

				} finally {
					if (!retained) {
						File tempFileDir = new File(tempFilePath);
						if (tempFileDir.exists()) {
							tempFileDir.deleteOnExit();
						}
					}
				}
			}
		}

	}

	private void executeJob(ConverterInfo converterInfo, String realInputFilePath) {
		ConverterExecutor openofficeExcutor = ConverterLocator.getMe().createConverterExecutor(ConverterExecutorEnum.OPENOFFICE_EXECUTOR);
		linuxed = openofficeExcutor.isLinuxos();
		while (linuxed && realInputFilePath.startsWith("/")) {
			realInputFilePath = realInputFilePath.substring(1, realInputFilePath.length());
		}

		try {
			String extName = FileConverterUtils.getFileSufix(realInputFilePath);
			if ("pdf".equals(extName)) {
				//---------对 pdf 文档添加水印、页眉、页脚、logo
				executeWatermark(converterInfo, realInputFilePath);
			} else {

				String outputFilePath = FileConverterUtils.getOutputFilePathByExtName(realInputFilePath, "pdf");
				LOGGER.info("Added watermark header footer logo outputFilePath ========>>>" + outputFilePath);

				//		while (linuxed && realOutputFilePath.startsWith("/")) {
				//			realOutputFilePath = realOutputFilePath.substring(1, realOutputFilePath.length());
				//		}

				//---------执行对 office(word\excel\ppt) 文档进行 pdf 转换
				openofficeExcutor.execute(realInputFilePath, outputFilePath);

				//---------对 pdf 文档添加水印、页眉、页脚、logo
				executeWatermark(converterInfo, outputFilePath);
			}

		} catch (Exception e) {
			e.printStackTrace();
			converterInfoManager.updateConverterInfoStatus(converterInfo.getKey(), ConverterStatusEnum.STATUS_FAIL);
		} finally {
			if (!retained) {
				File tempFileDir = new File(realInputFilePath);
				if (tempFileDir.exists()) {
					tempFileDir.deleteOnExit();
				}
			}
		}
	}

	/**
	 * 为转换的PDF添加水印、页眉、页脚和logo
	 * 
	 * @param converterInfo
	 * @param inputFilePath
	 */
	private void executeWatermark(ConverterInfo converterInfo, String inputFilePath) {

		boolean watermarkImaged = ConverterConfiguration.getMe().getConverterPdfWatermarkImaged();

		String converterDir = ConverterConfiguration.getMe().getConverterExecuteDir();

		//----------水印图片目录
		StringBuffer watermark = new StringBuffer();
		String watermarkValue;
		if (watermarkImaged) {
			watermarkValue = ConverterConfiguration.getMe().getConverterPdfWatermarkImage();
			//如果水印是图片，构造图片文件路径
			watermark.append(converterDir);
			watermark.append("/");
			watermark.append(WATERMARKPATH);
			watermark.append("/");
			File watermarkFileDir = new File(watermark.toString());
			if (!watermarkFileDir.exists()) {
				LOGGER.info("=========>>> Directory not exist. Create it.");
				LOGGER.info("=========>>> " + watermarkFileDir);
				watermarkFileDir.mkdirs();
			}
		} else {
			watermarkValue = ConverterConfiguration.getMe().getConverterPdfWatermarkText();
		}
		watermark.append(watermarkValue);

		//----------logo图片目录
		StringBuffer logoImgFilePath = new StringBuffer(converterDir);
		logoImgFilePath.append("/");
		logoImgFilePath.append(LOGOPATH);
		logoImgFilePath.append("/");
		File logoFileDir = new File(logoImgFilePath.toString());
		if (!logoFileDir.exists()) {
			LOGGER.info("========>>> Directory not exist. Create it.");
			LOGGER.info("========>>> " + logoFileDir);
			logoFileDir.mkdirs();
		}
		String logoImageValue = ConverterConfiguration.getMe().getConverterPdfLogoImage();
		logoImgFilePath.append(logoImageValue);

		String header = ConverterConfiguration.getMe().getConverterPdfHeader();
		String footer = ConverterConfiguration.getMe().getConverterPdfFooter();
		//LOGGER.info("1header=====>>> " + header + "  1footer=====>>> " + footer);

		//----------实例化水印转换执行器
		WatermarkConverterExecutor watermarkExecutor = new WatermarkConverterExecutor(watermark.toString(), watermarkImaged, header,
				footer, logoImgFilePath.toString());
		String outputFilePath = FileConverterUtils.getOutputFilePathByFileName(inputFilePath, converterInfo.getKey() + ".pdf");
		LOGGER.info("Added watermark header footer logo outputFilePath ========>>>" + outputFilePath);

		try {

			//---------执行添加水印、页眉、页脚、logo 的处理
			watermarkExecutor.execute(inputFilePath, outputFilePath);

			//---------对 pdf 文档进行截图
			executeCaptureImage(converterInfo, outputFilePath);

		} catch (Exception e) {
			e.printStackTrace();
			converterInfoManager.updateConverterInfoStatus(converterInfo.getKey(), ConverterStatusEnum.STATUS_FAIL);
		} finally {
			if (!retained) {
				File tempFileDir = new File(inputFilePath);
				if (tempFileDir.exists()) {
					tempFileDir.deleteOnExit();
				}
			}
		}
	}

	/**
	 * 为转换的PDF文档进行截图，作为缩略图
	 * 
	 * @param converterInfo
	 * @param inputFilePath
	 */
	private void executeCaptureImage(ConverterInfo converterInfo, String inputFilePath) {

		int count = ConverterConfiguration.getMe().getConverterCaptureImageCount();
		float zoom = ConverterConfiguration.getMe().getConverterCaptureImageZoom();
		CaptureImageConverterExecutor captureImgExecutor = new CaptureImageConverterExecutor(count, zoom);

		String fileName = converterInfo.getKey() + ".jpg";
		String outputFilePath = FileConverterUtils.getOutputFilePathByFileName(inputFilePath, fileName);
		LOGGER.info("Cature Image outputFilePath ========>>>" + outputFilePath);

		try {
			//---------执行 pdf 文档进行截图
			captureImgExecutor.execute(inputFilePath, outputFilePath);

			//---------对 pdf 文档进行swf转换
			executeSwftools(converterInfo, inputFilePath);

			//----------截图作为转换文件的缩略图，并上传到文件系统
			File destFile = new File(outputFilePath);
			if (destFile.exists()) {
				String fileId = FastDFSClient.uploadFile(destFile, fileName);
				if (StringUtils.isNotBlank(fileId)) {
					LOGGER.info("截图文件已上传==========>>> " + fileId);
					converterInfo.setCaptureImgPath(fileId);
				} else {
					LOGGER.info("截图文件上传失败 ==========>>> " + fileId);
				}
			} else {
				LOGGER.info("文件不存在截图失败 ==========>>> " + destFile.getAbsolutePath());
			}

		} catch (Exception e) {
			e.printStackTrace();
			converterInfoManager.updateConverterInfoStatus(converterInfo.getKey(), ConverterStatusEnum.STATUS_FAIL);
		} finally {
			if (!retained) {
				File tempFileDir = new File(inputFilePath);
				if (tempFileDir.exists()) {
					tempFileDir.deleteOnExit();
				}
				tempFileDir = new File(outputFilePath);
				if (tempFileDir.exists()) {
					tempFileDir.deleteOnExit();
				}
			}
		}
	}

	/**
	 * 为转换的PDF文档，再次进行转换为swf文件
	 * 
	 * @param converterInfo
	 * @param inputFilePath
	 */
	private void executeSwftools(ConverterInfo converterInfo, String inputFilePath) {

		ConverterExecutor swftoolsExcutor = ConverterLocator.getMe().createConverterExecutor(ConverterExecutorEnum.SWFTOOLS_EXECUTOR);
		String outputFilePath = FileConverterUtils.getOutputFilePathByFileName(inputFilePath, converterInfo.getKey() + ".swf");
		LOGGER.info("Convert to swf outputFilePath ========>>>" + outputFilePath);

		try {

			//---------执行对 pdf 文档进行swf转换
			swftoolsExcutor.execute(inputFilePath, outputFilePath);

			//---------将转换出来的swf文件上传至文件系统
			File destFile = new File(outputFilePath);
			if (destFile.exists()) {
				String fileId = FastDFSClient.uploadFile(destFile, converterInfo.getKey() + ".jpg");
				if (StringUtils.isNotBlank(fileId)) {

					LOGGER.info("swf文件已上传==========>>> " + fileId);
					converterInfo.setDestFilePath(fileId);

				} else {
					LOGGER.info("swf文件上传失败 ==========>>> " + fileId);
				}
			} else {
				LOGGER.info("文件不存在，转换swf失败 ==========>>> " + destFile.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			converterInfoManager.updateConverterInfoStatus(converterInfo.getKey(), ConverterStatusEnum.STATUS_FAIL);
		} finally {
			if (!retained) {
				File tempFileDir = new File(inputFilePath);
				if (tempFileDir.exists()) {
					tempFileDir.deleteOnExit();
				}
				tempFileDir = new File(outputFilePath);
				if (tempFileDir.exists()) {
					tempFileDir.deleteOnExit();
				}
			}
		}
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
