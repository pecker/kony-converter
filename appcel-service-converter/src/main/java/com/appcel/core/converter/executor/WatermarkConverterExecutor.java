/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.executor;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.appcel.facade.converter.exception.ConverterException;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfCopy.PageStamp;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-18
 */
public class WatermarkConverterExecutor implements ConverterExecutor {

	protected static Log LOGGER = LogFactory.getLog(WatermarkConverterExecutor.class);

	/**
	 * watermarkImged如果是true，watermark则为水印图片的路径，否则为水印的文字
	 */
	private String watermark;
	private boolean watermarkImaged = true;
	private String header;
	private String footer;
	private String logoImgPath;

	//private boolean linuxed;

	public WatermarkConverterExecutor(String watermark, boolean watermarkImaged, String header, String footer, String logoImgPath) {
		this.watermark = watermark;
		this.header = header;
		this.footer = footer;
		this.watermarkImaged = watermarkImaged;
		this.logoImgPath = logoImgPath;
		//this.linuxed = linuxed;
	}

	public boolean isLinuxos() {
		throw new ConverterException("not support...");
	}

	/**
	 *	添加水印、页眉、页脚
	 * @param fileName 源文件路径
	 * @param savepath 目标文件路径
	 * @param watermark 文字水印
	 * @param header 页眉
	 * @param footer 页脚
	 */
	public void execute(String inputFilePath, String outputFilePath) throws ConverterException {
		if (StringUtils.isBlank(inputFilePath) || StringUtils.isBlank(outputFilePath)) {
			throw new ConverterException("文档转换异常",
					new Throwable("inputFilePath is not null.", new Throwable("outputFilePath is not null.")));
		}

		LOGGER.info("==========>>> 开始为PDF文档添加水印、logo、页眉、页脚...");

		// 文档总页数
		int num = 0;
		Document document = new Document();
		try {
			PdfReader reader = new PdfReader(inputFilePath);
			BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
			//BaseFont base = BaseFont.createFont(BaseFont.COURIER, "utf-8", BaseFont.EMBEDDED);

			num = reader.getNumberOfPages();
			PdfCopy copy = new PdfCopy(document, new FileOutputStream(outputFilePath));
			document.open();
			for (int i = 0; i < num;) {
				PdfImportedPage page = copy.getImportedPage(reader, ++i);
				PageStamp stamp = copy.createPageStamp(page);
				Font font = new Font(base, 10, Font.NORMAL);

				// 添加页脚，左侧文字，右侧页码
				ColumnText.showTextAligned(stamp.getUnderContent(), Element.ALIGN_RIGHT, new Phrase(String.format("第 %d 页/共 %d 页", i, num),
						font), 550f, 28, 0);

				//LOGGER.info("2header=====>>> " + header + "  2footer=====>>> " + footer);
				// 添加页眉 (文字页眉，居中)
				ColumnText.showTextAligned(stamp.getUnderContent(), Element.ALIGN_CENTER, new Phrase(header, font), 150f, 800, 0);
				// 添加页脚
				ColumnText.showTextAligned(stamp.getUnderContent(), Element.ALIGN_LEFT, new Phrase(footer, font), 50f, 28, 0);

				// 页眉添加logo （图片页眉，居右）"F:\\Tools\\pdf2swf工具\\resource/logo.png"
				Image logoImg = Image.getInstance(logoImgPath);//"template/logo.png");// 选择图片
				logoImg.setAlignment(1);
				logoImg.scaleAbsolute(436 / 5, 96 / 5);// 控制图片大小
				logoImg.setAbsolutePosition(450f, 800);// 控制图片位置
				stamp.getUnderContent().addImage(logoImg);

				// 设置水印倾斜 开始
				float pageWidth = reader.getPageSize(i).getWidth();
				float pageHeight = reader.getPageSize(i).getHeight();

				// 添加水印
				if (watermarkImaged) {
					// 水印是图片的处理 "F:\\Tools\\pdf2swf工具\\resource/watermark.png"
					Image watermarkImg = Image.getInstance(watermark);//"template/logo.png");// 选择图片
					watermarkImg.setAlignment(1);
					watermarkImg.scaleAbsolute(90, 45);// 控制图片大小 436 / 5, 96 / 5
					watermarkImg.setAbsolutePosition((pageWidth - 8) / 2, pageHeight / 2);// 控制图片位置 pageWidth / 2, pageHeight / 2
					watermarkImg.setRotation(120);
					stamp.getUnderContent().addImage(watermarkImg);
				} else {
					//水印是文字的处理					
					PdfContentByte under = stamp.getUnderContent();
					under.beginText();
					under.setColorFill(Color.LIGHT_GRAY);
					// 字符越长，字体越小，设置字体
					int fontSize = getFontSize(watermark);
					under.setFontAndSize(base, fontSize);

					// 水印文字成60度角倾斜,且页面居中展示
					under.showTextAligned(Element.ALIGN_CENTER, watermark, pageWidth / 2, pageHeight / 2, 60);

					// 字体设置结束
					under.endText();
				}

				stamp.alterContents();
				copy.addPage(page);
				LOGGER.info("==========>>> 添加水印、logo、页眉、页脚成功.");
			}
		} catch (Exception e) {
			LOGGER.info("==========>>> 添加水印、logo、页眉、页脚失败.");
			e.printStackTrace();

		} finally {
			if (null != document) {
				document.close();
				document = null;
			}
		}
		LOGGER.info("==========>>> pdf totalpages:" + num);
	}

	/**
	 *	添加水印、页眉、页脚
	 * @param fileName 源文件路径
	 * @param savepath 目标文件路径
	 * @param watermark 文字水印
	 * @param header 页眉
	 * @param footer 页脚
	 * @return
	 */
	public void execute(File inputFile, File outputFile) {
		if (!inputFile.exists()) {
			// 找不到源文件, 则直接抛出异常
			throw new ConverterException("文件不存在", new Throwable("inputFile is null.", new Throwable("inputFile文件没找到.")));
		}
		execute(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
	}

	/**
	 * 根据水印文字长度计算获取字体大小
	 * @param watermark
	 * @return
	 */
	private int getFontSize(String watermark) {
		int fontSize = 80;
		if (StringUtils.isNotBlank(watermark)) {
			int length = watermark.length();
			if (length <= 26 && length >= 18) {
				fontSize = 26;
			} else if (length < 18 && length >= 8) {
				fontSize = 40;
			} else if (length < 8 && length >= 1) {
				fontSize = 80;
			} else {
				fontSize = 16;
			}
		}
		return fontSize;
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
