/*
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * Encoder A Java Audio/Video Encoder (based on FFMPEG)
 * 
 * Copyright (C) 2006-2014 LiRongSheng (http://www.pointdew.com)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.pointdew.com>.
 */
package com.appcel.core.converter.locator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.appcel.core.converter.config.ConverterConfiguration;
import com.appcel.core.converter.executor.ConverterExecutor;
import com.appcel.core.converter.executor.OpenofficeConverterExecutor;
import com.appcel.core.converter.executor.SwftoolsConverterExecutor;
import com.appcel.facade.converter.enums.ConverterExecutorEnum;
import com.appcel.facade.converter.exception.ConverterException;

/**
 * 
 * Defiend class file the DefaultFFMPEGLocator.java
 * 
 * The default ffmpeg executable locator, which exports on disk the ffmpeg
 * executable bundled with the library distributions. It should work both for
 * windows and many linux distributions. If it doesn't, try compiling your own
 * ffmpeg executable and plug it in JAVE with a custom {@link AbstractConverterLocator}.
 *   
 * @author Rock.Lee
 * @version appcel 1.0.0
 * @since JDK-1.7.0
 * @date 2014-12-2
 */
public class ConverterLocator extends AbstractConverterLocator {
	protected static Log LOGGER = LogFactory.getLog(ConverterLocator.class);
	/**
	 * Trace the version of the bundled ffmpeg executable. It's a counter: every
	 * time the bundled ffmpeg change it is incremented by 1.
	 */
	//private static final int myEXEversion = 1;

	private static ConverterLocator encoderLocator = new ConverterLocator();

	/**
	 * It builds the default FFMPEGLocator, exporting the ffmpeg executable on a
	 * temp file.
	 */
	private ConverterLocator() {

	}

	public static ConverterLocator getMe() {
		return encoderLocator;
	}

	@Override
	public ConverterExecutor createConverterExecutor(ConverterExecutorEnum executorEnum) {
		ConverterExecutor converterExcutor = null;

		String os = System.getProperty("os.name").toLowerCase();
		// Windows?
		boolean isWindows = (os.indexOf("windows") != -1) ? true : false; // Need a chmod?

		//String archexe = executorEnum.getValue();

		// Temp dir?
		//		File temp = new File(System.getProperty("java.io.tmpdir"), archexe + myEXEversion);
		//		if (!temp.exists()) {
		//			temp.mkdirs();
		//			temp.deleteOnExit();
		//		}

		// converter executable export on disk.
		//		String suffix = isWindows ? ".exe" : "";
		//		File exeFile = new File(temp, archexe + suffix);

		//String executableCmdPath = exeFile.getAbsolutePath();

		//		if (!exeFile.exists()) {
		//
		//			copyFile(archexe + suffix, exeFile);
		//		}
		ConverterConfiguration converterConf = ConverterConfiguration.getMe();

		String openofficeHome = null;
		String swftoolsHome = null;
		String swftoolsExecutor = null;
		String swftoolsFontHome = null;

		if (ConverterExecutorEnum.OPENOFFICE_EXECUTOR.equals(executorEnum)) {

			openofficeHome = isWindows ? converterConf.getConverterOpenofficeWinHome() : converterConf.getConverterOpenofficeLinuxHome();

			converterExcutor = new OpenofficeConverterExecutor(openofficeHome, ConverterConfiguration.getMe().getConverterOpenofficePort(),
					!isWindows);

			LOGGER.info("Converter openoffice home Path 转换器所在位置==========>>> " + openofficeHome);

		} else if (ConverterExecutorEnum.SWFTOOLS_EXECUTOR.equals(executorEnum)) {

			swftoolsHome = isWindows ? converterConf.getConverterSwftoolsWinHome() : converterConf.getConverterSwftoolsLinuxHome();
			swftoolsFontHome = isWindows ? converterConf.getConverterSwftoolsFontWinHome() : converterConf
					.getConverterSwftoolsFontLinuxHome();
			swftoolsExecutor = isWindows ? converterConf.getConverterSwftoolsWinExecutor() : converterConf
					.getConverterSwftoolsLinuxExecutor();
			converterExcutor = new SwftoolsConverterExecutor(swftoolsHome, swftoolsFontHome, swftoolsExecutor, !isWindows);

			LOGGER.info("Converter swftools home Path 转换器所在位置==========>>> " + swftoolsHome);
			LOGGER.info("Converter swfls font home Path 转换器所在位置==========>>> " + swftoolsFontHome);
			LOGGER.info("Converter swftools executor 转换执行器==========>>> " + swftoolsExecutor);

		} else {
			throw new ConverterException("Illegal The encoder paramter executor error : " + executorEnum + " is not support.");
		}

		if (!isWindows) {
			Runtime runtime = Runtime.getRuntime();
			try {
				if (StringUtils.isNotBlank(swftoolsFontHome)) {
					runtime.exec(new String[] { "/bin/chmod", "755", swftoolsFontHome });
				}
				if (StringUtils.isNotBlank(swftoolsExecutor)) {
					runtime.exec(new String[] { "/bin/chmod", "755", swftoolsExecutor });
				}

			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.info(" Linux chmod 755 error. ==========>>>" + e.getCause());
			}
		}

		return converterExcutor;
	}

	/**
	  * 获取OpenOffice安装目录
	  * @return
	 */
	protected String getOfficeHome() {
		String osName = System.getProperty("os.name");
		if (Pattern.matches("Linux.*", osName)) {
			return "/opt/openoffice.org3";
		} else if (Pattern.matches("Windows.*", osName)) {
			return "C:/Program Files (x86)/OpenOffice 4";//"E:/software/OpenOffice 4";
		} else if (Pattern.matches("Mac.*", osName)) {
			return "/Application/OpenOffice.org.app/Contents";
		}
		return null;
	}

	/** * Copies a file bundled in the package to the supplied destination.
	 * 
	 * @param path
	 *            The name of the bundled file.
	 * @param dest
	 *            The destination.
	 * @throws RuntimeException
	 *             If aun unexpected error occurs.
	 */
	protected void copyFile(String path, File dest) throws RuntimeException {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = getClass().getResourceAsStream(path);
			output = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int l;
			while ((l = input.read(buffer)) != -1) {
				output.write(buffer, 0, l);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot write file " + dest.getAbsolutePath());
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Throwable t) {
					;
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (Throwable t) {
					;
				}
			}
		}
	}
}
