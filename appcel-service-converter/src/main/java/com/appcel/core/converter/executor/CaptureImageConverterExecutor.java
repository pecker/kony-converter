/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.executor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.appcel.facade.converter.exception.ConverterException;

/**
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-18
 */
public class CaptureImageConverterExecutor implements ConverterExecutor {
	protected static Log LOGGER = LogFactory.getLog(CaptureImageConverterExecutor.class);
	private int count;
	private float zoom;

	//private boolean linuxed;

	public CaptureImageConverterExecutor(int count, float zoom) {
		this.count = count;
		this.zoom = zoom;
		//this.linuxed = linuxed;
	}

	public boolean isLinuxos() {
		throw new ConverterException("method unimplements, not support... ");
	}

	public void execute(String inputFilePath, String outputFilePath) throws ConverterException {
		if (StringUtils.isBlank(inputFilePath) || StringUtils.isBlank(outputFilePath)) {
			throw new ConverterException("文档转换异常", new Throwable("inputFilePath can not is null or empty.", new Throwable(
					"outputFilePath can not is null or empty.")));
		}

		// ICEpdf document class
		Document document = new Document();
		float rotation = 0f;
		FileOutputStream out = null;
		ImageOutputStream outImage = null;

		LOGGER.info("==========>>> 开始为PDF文档进行截图处理...");

		try {

			document.setFile(inputFilePath);

			int maxPages = document.getPageTree().getNumberOfPages();
			if (count > maxPages || count < 0) {
				throw new ConverterException("参数截图数量 count :" + count + " 与文档总页数 pages : " + maxPages + " 不符");
			}
			if (count == 0) {
				count = 1;
			}
			for (int i = 0; i < maxPages; i++) {

				if (count != 0 && i + 1 != count) {
					continue;
				}

				BufferedImage img = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX,
						rotation, zoom);
				Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("jpg");
				ImageWriter writer = (ImageWriter) iter.next();

				File outputFile = new File(outputFilePath);
				//				String filename = i + "";
				//				if (i + 1 == count) {
				//					File srcFile = new File(inputFilePath);
				//					filename = srcFile.getName();
				//				}
				//				
				//				if (outputFile.exists()) {
				//					outputFile.mkdirs();
				//					if (outputFile.isDirectory()) {
				//						outputFile = new File(outputFilePath + "/" + filename + ".jpg");
				//					}
				//
				//				} else {
				//					if (outputFile.isDirectory()) {
				//						outputFile = new File(outputFilePath + "/" + filename + ".jpg");
				//					}
				//				}

				out = new FileOutputStream(outputFile);
				outImage = ImageIO.createImageOutputStream(out);
				writer.setOutput(outImage);
				writer.write(new IIOImage(img, null, null));
				img.flush();

				//BufferedImage img = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX,
				//		rotation, zoom);
				//BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				//tag.getGraphics().drawImage(img, 0, 0, width, height, null);
				//out = new FileOutputStream(outputFilePath);
				//ImageIO.write(img, "jpg", out);
				//img.flush();
				//byte[] data = out.toByteArray();
				//return new ByteArrayInputStream(data);
				LOGGER.info("==========>>> 为PDF文档截图成功.");
			}
		} catch (PDFException e) {
			LOGGER.info("==========>>> 为PDF文档失败.");
			e.printStackTrace();
		} catch (PDFSecurityException e) {
			LOGGER.info("==========>>> 为PDF文档失败.");
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.info("==========>>> 为PDF文档失败.");
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
				}
			}
			if (outImage != null) {
				try {
					outImage.close();
					outImage = null;
				} catch (IOException e) {
				}
			}
			if (document != null) {
				document.dispose();
				document = null;
			}
		}
	}

	public void execute(File inputFile, File outputFile) throws ConverterException {
		if (null == inputFile || !inputFile.exists() || null == outputFile) {
			throw new ConverterException("文件不存在", new Throwable("inputFilePath or outputFile can not is null.", new Throwable(
					"inputFile文件没找到.")));
		}
		execute(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
	}

}
