/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.pointdew.common.io.IOUtil;

/**
 * 
 * Defiend class file the CaptureImage.java
 * 
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-8-1
 */
public class CaptureImage {
	/**
	 * 
	 * @param srcFilePath 要截图的pdf文件
	 * @param zoom
	 *            缩略图显示倍数，1表示不缩放，0.3则缩小到30%
	 * @param count
	 *            pdf页面代码，如果为0则全部转换，不然转换指定页，如果大于PDF文档页数，将不处理截图
	 * @return 返回指定某个截图的流数据
	 */
	public static InputStream screenshotThumbnail(String srcFilePath, float zoom, int count) {
		// ICEpdf document class
		Document document = new Document();
		float rotation = 0f;
		ByteArrayOutputStream out = null;
		try {
			document.setFile(srcFilePath);

			int maxPages = document.getPageTree().getNumberOfPages();
			if (count > maxPages || count < 0) {
				return null;
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

				//BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				//tag.getGraphics().drawImage(img, 0, 0, width, height, null);

				out = new ByteArrayOutputStream();
				ImageIO.write(img, "jpg", out);
				img.flush();
				byte[] data = out.toByteArray();
				return new ByteArrayInputStream(data);
			}
		} catch (PDFException e) {
			e.printStackTrace();
		} catch (PDFSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
				}
			}
			if (document != null) {
				document.dispose();
				document = null;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param srcFilePath
	 *            原文件路径
	 * @param distDirPath
	 *            目标目录路径
	 * @param zoom
	 *            缩略图显示倍数，1表示不缩放，0.3则缩小到30%
	 * @param count
	 *            pdf页面代码，如果为0则全部转换，不然转换指定页，如果大于PDF文档页数，将不处理截图
	 */
	public static boolean screenshotThumbnail(String srcFilePath, String distDirPath, float zoom, int count) {

		float rotation = 0f;

		FileOutputStream out = null;
		ImageOutputStream outImage = null;
		// ICEpdf document class
		Document document = new Document();

		try {
			document.setFile(srcFilePath);

			int maxPages = document.getPageTree().getNumberOfPages();
			if (count > maxPages || count < 0) {
				return false;
			}
			if (count == 0) {
				count = 1;
			}
			for (int i = 0; i < maxPages; i++) {

				String filename = i + "";
				if (count != 0 && i + 1 != count) {
					continue;
				}
				if (i + 1 == count) {
					File srcFile = new File(srcFilePath);
					filename = srcFile.getName();
				}
				BufferedImage img = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX,
						rotation, zoom);
				Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("jpg");
				ImageWriter writer = (ImageWriter) iter.next();
				File outFile = new File(distDirPath + "/" + filename + ".jpg");
				File dir = new File(distDirPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				out = new FileOutputStream(outFile);
				outImage = ImageIO.createImageOutputStream(out);
				writer.setOutput(outImage);
				writer.write(new IIOImage(img, null, null));
				img.flush();
			}
			return true;
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
			if (outImage != null) {
				try {
					outImage.close();
				} catch (IOException e) {
				}
			}
			if (document != null) {
				document.dispose();
			}
		}
	}

	/*
	 * 进行测试转换是否成功
	 * 
	 */
	public static void main(String[] args) {
		//"d:\\resource\\sakai教学平台介绍.pdf";
		String srcPdfFilePath = "D:\\Tools\\pdf2swf工具\\resource\\sakai教学平台介绍.pdf";
		//String srcPdfFilePath ="D:\\Tools\\pdf2swf工具\\resource\\新疆虚拟仿真实训平台开发工作量评估.pdf";
		String distFlePath = "D:\\Tools\\pdf2swf工具\\resource";
		try {
			//screenshotThumbnail(srcPdfFilePath, distFlePath, 0.3f, 2);

			InputStream inputStream = screenshotThumbnail(srcPdfFilePath, 0.3f, 1);
			File file = new File(distFlePath + "/testPdfCaptionImage.jpg");
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			IOUtil.pipe(inputStream, fileOutputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
