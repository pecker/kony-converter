package com.appcel.facade.converter.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 
 * Defiend class file the EncoderRuntimeException.java
 * 
 * 
 * @author Rock.Lee
 * @version cel 1.0.0
 * @since JDK-1.7.0
 * @date 2014年1月20日下午2:16:23
 */
public class ConverterException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Throwable cause = null;

	/**
	 * Constructor for KernelRuntimeException.
	 */
	public ConverterException() {
		super();
	}

	/**
	 * Constructor for KernelRuntimeException.
	 * 
	 * @param message
	 */
	public ConverterException(String message) {
		super(message);
	}

	/**
	 * Constructor for KernelRuntimeException.
	 * 
	 * @param message
	 * @param cause
	 */
	public ConverterException(String message, Throwable cause) {
		super(message);
		this.cause = cause;
	}

	/**
	 * Constructor for KernelRuntimeException.
	 * 
	 * @param cause
	 */
	public ConverterException(Throwable cause) {
		super();
		this.cause = cause;
	}

	/**
	 * @return the cause of this exception or null if nonexistent
	 */
	public Throwable getCause() {
		return cause;
	}

	/**
	 * Set the cause of this exception
	 */
	public Throwable initCause(Throwable cause) {
		this.cause = cause;
		return this.cause;
	}

	/**
	 * Print this throwable and its backtrace
	 */
	public void printStackTrace() {
		if (cause != null) {
			cause.printStackTrace();
		}
		super.printStackTrace();
	}

	/**
	 * Print this throwable and its backtrace using a PrintStream object
	 */
	public void printStackTrace(PrintStream s) {
		if (cause != null) {
			cause.printStackTrace(s);
		}
		super.printStackTrace(s);
	}

	/**
	 * Print this throwable and its backtrace using a PrintWriter object
	 */
	public void printStackTrace(PrintWriter w) {
		if (cause != null) {
			cause.printStackTrace(w);
		}
		super.printStackTrace(w);
	}

}
