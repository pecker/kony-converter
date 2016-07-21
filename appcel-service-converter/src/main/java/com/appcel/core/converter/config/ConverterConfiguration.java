/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.core.converter.config;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 
 * Defiend class file the ConverterConfiguration.java
 * 
 * 文档转换环境配置 
 *  
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-7-24
 */
public class ConverterConfiguration {

	private static Logger LOGGER = Logger.getLogger(ConverterConfiguration.class);

	//## **********文档转换环境变量配置***********
	private static final String CONVERTER_OPENOFFICE_HOST = "converter.openoffice.host";
	private static final String CONVERTER_OPENOFFICE_PORT = "converter.openoffice.port";

	private static final String CONVERTER_OPENOFFICE_WIN_HOME = "converter.openoffice.win.home";
	private static final String CONVERTER_OPENOFFICE_LINUX_HOME = "converter.openoffice.linux.home";
	private static final String CONVERTER_OPENOFFICE_MAC_HOME = "converter.openoffice.mac.home";

	private static final String CONVERTER_SWFTOOLS_WIN_HOME = "converter.swftools.win.home";
	private static final String CONVERTER_SWFTOOLS_LINUX_HOME = "converter.swftools.linux.home";
	private static final String CONVERTER_SWFTOOLS_FONT_WIN_HOME = "converter.swftools.font.win.home";
	private static final String CONVERTER_SWFTOOLS_FONT_LINUX_HOME = "converter.swftools.font.linux.home";
	private static final String CONVERTER_SWFTOOLS_WIN_EXECUTOR = "converter.swftools.win.executor";
	private static final String CONVERTER_SWFTOOLS_LINUX_EXECUTOR = "converter.swftools.linux.executor";

	//## 文档截图大小，截图的缩放值, 配置的值必须是 float 类型，默认值：0.3f
	private static final String CONVERTER_CAPTURE_IMAGE_ZOOM = "converter.capture.image.zoom";
	//## 文档截图张数, 默认值：1
	private static final String CONVERTER_CAPTURE_IMAGE_COUNT = "converter.capture.image.count";

	/* 为pdf文档添加水印、页眉、页脚、logo */
	private static final String CONVERTER_PDF_WATERMARK_IMAGED = "converter.pdf.watermark.imaged";
	private static final String CONVERTER_PDF_WATERMARK_TEXT = "converter.pdf.watermark.text";//我是水印
	private static final String CONVERTER_PDF_WATERMARK_IMAGE = "converter.pdf.watermark.image";//=watermark.png
	private static final String CONVERTER_PDF_LOGO_IMAGE = "converter.pdf.logo.image";//=logo.png
	private static final String CONVERTER_PDF_HEADER = "converter.pdf.header";//我是页眉
	private static final String CONVERTER_PDF_FOOTER = "converter.pdf.footer";//我的页脚

	//## ********* 编码器转码目录配置 *********
	private static final String CONVERTER_EXECUTE_DIR = "converter.execute.dir";
	//## ********* 转码后的文件是否保留，默认false，不保留，转码完成后即会删除转码后的文件，以节省磁盘  *********
	private static final String CONVERTER_FILE_RETAINED = "converter.file.retained";

	private static final String CONVERTER_CONF_FILE = "converter.properties";

	private static Map<String, String> converterConfMap = new HashMap<String, String>();
	private static ConverterConfiguration converterconf = new ConverterConfiguration();

	public static ConverterConfiguration getMe() {
		return converterconf;
	}

	private ConverterConfiguration() {
		loadEncoderConfigInside();
	}

	public void loadEncoderConfigInside() {
		loadConfig(CONVERTER_CONF_FILE);
	}

	private void loadConfig(String name) {
		Properties props = new Properties();
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(name), "utf-8");
			props.load(reader);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Exception Message =========>>> " + e.getMessage());
			LOGGER.info("Exception Cause =========>>> " + e.getCause());
		}
		try {
			Set<Object> set = props.keySet();
			Iterator<Object> it = set.iterator();
			LOGGER.info(name);
			while (it.hasNext()) {
				String key = it.next().toString();
				Object value = props.get(key);
				converterConfMap.put((String) key, (String) value);
				LOGGER.info(key + "=" + value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Exception Message =========>>> " + e.getMessage());
			LOGGER.info("Exception Cause =========>>> " + e.getCause());
		}
	}

	public String getConverterOpenofficeHost() {
		return getValue(CONVERTER_OPENOFFICE_HOST, "127.0.0.1");
	}

	public int getConverterOpenofficePort() {
		int port = Integer.parseInt(getValue(CONVERTER_OPENOFFICE_PORT, "8100"));
		return port;
	}

	public String getConverterOpenofficeWinHome() {
		return getValueByKey(CONVERTER_OPENOFFICE_WIN_HOME);
	}

	public String getConverterOpenofficeLinuxHome() {
		return getValueByKey(CONVERTER_OPENOFFICE_LINUX_HOME);
	}

	public String getConverterOpenofficeMacHome() {
		return getValueByKey(CONVERTER_OPENOFFICE_MAC_HOME);
	}

	public String getConverterSwftoolsWinHome() {
		return getValueByKey(CONVERTER_SWFTOOLS_WIN_HOME);
	}

	public String getConverterSwftoolsLinuxHome() {
		return getValueByKey(CONVERTER_SWFTOOLS_LINUX_HOME);
	}

	public String getConverterSwftoolsFontWinHome() {
		return getValueByKey(CONVERTER_SWFTOOLS_FONT_WIN_HOME);
	}

	public String getConverterSwftoolsFontLinuxHome() {
		return getValueByKey(CONVERTER_SWFTOOLS_FONT_LINUX_HOME);
	}

	public String getConverterSwftoolsWinExecutor() {
		return getValueByKey(CONVERTER_SWFTOOLS_WIN_EXECUTOR);
	}

	public String getConverterSwftoolsLinuxExecutor() {
		return getValueByKey(CONVERTER_SWFTOOLS_LINUX_EXECUTOR);
	}

	public float getConverterCaptureImageZoom() {
		float zoom = Float.parseFloat(getValue(CONVERTER_CAPTURE_IMAGE_ZOOM, "0.3f"));
		return zoom;
	}

	public int getConverterCaptureImageCount() {
		int count = Integer.parseInt(getValue(CONVERTER_CAPTURE_IMAGE_COUNT, "1"));
		return count;
	}

	public boolean getConverterFileRetained() {
		return "true".equals(getValueByKey(CONVERTER_FILE_RETAINED));
	}

	public String getConverterConfFile() {
		return getValueByKey(CONVERTER_CONF_FILE);
	}

	public boolean getConverterPdfWatermarkImaged() {
		return "true".equals(getValueByKey(CONVERTER_PDF_WATERMARK_IMAGED));
	}

	public String getConverterPdfWatermarkText() {
		return getValueByKey(CONVERTER_PDF_WATERMARK_TEXT);
	}

	public String getConverterPdfWatermarkImage() {
		return getValueByKey(CONVERTER_PDF_WATERMARK_IMAGE);
	}

	public String getConverterPdfLogoImage() {
		return getValueByKey(CONVERTER_PDF_LOGO_IMAGE);
	}

	public String getConverterPdfHeader() {
		return getValueByKey(CONVERTER_PDF_HEADER);
	}

	public String getConverterPdfFooter() {
		return getValueByKey(CONVERTER_PDF_FOOTER);
	}

	/**
	 * 根据 Key 获取编码转换 config 信息
	 * 
	 * @param key
	 * @param defaultValue 默认值
	 * @return
	 */
	public String getValue(String key, String defaultValue) {
		String value = converterConfMap.get(key) == null ? defaultValue : converterConfMap.get(key);
		//		try {
		//			return URLDecoder.decode(value, "utf-8");
		//		} catch (UnsupportedEncodingException e) {
		//			LOGGER.info("getValueByKey =====>>> URLDecoder error.");
		//			e.printStackTrace();
		//		}
		return value;
	}

	public String getValueByKey(String key) {
		return getValue(key, "");
	}

	/**
	 * @return
	 */
	public String getConverterExecuteDir() {
		String converterdir = this.getValue(CONVERTER_EXECUTE_DIR, "/converterdir");
		try {
			converterdir = URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("").getPath() + converterdir,
					"utf-8");
			LOGGER.info(converterdir);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return converterdir;
	}
}
