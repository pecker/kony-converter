/**
 * Copyright (c) 2006, Pointdew Inc. All rights reserved.
 * 
 * http://www.pointdew.com
 */
package com.appcel.facade.converter.mode;

import java.util.Date;

import com.appcel.facade.converter.enums.ConverterStatusEnum;
import com.appcel.facade.converter.enums.ConverterTypeEnum;
import com.appcel.kernel.mybatis.model.BaseModel;

/**
 * 文档转换业务数据模型
 *  
 * @author Kony.Lee
 * @version appcel 1.0
 * @since JDK-1.7.0
 * @date 2016-5-11
 */
public class ConverterInfo extends BaseModel {
	private static final long serialVersionUID = 1L;

	/**
	 * 转换文件名
	 */
	private String fileName;
	/**
	 * 转换文件播放模式：word\excel\ppt\pdf
	 */
	private Integer converterType = ConverterTypeEnum.WORD.getValue();

	/**
	 * 原始文件存放路径
	 */
	private String srcFilePath;

	/**
	 * 转换后文件存放路径
	 */
	private String destFilePath;

	/**
	 * 截图文件存放路径
	 */
	private String captureImgPath;

	/**
	 * 关联的资源或者datastream的key
	 */
	private String entityKey;
	/**
	 * 文件转换状态
	 */
	private Integer status = ConverterStatusEnum.STATUS_WAIT_UPLOAD.getValue();
	/**
	 * 注意事项
	 */
	private String remark;

	/**
	 * 最后转换完成时间
	 */
	protected Date converterTime;

	public ConverterInfo() {
	}

	public ConverterInfo(String entityKey, String fileName, String srcFilePath, Integer status, String remark) {
		this.entityKey = entityKey;
		this.fileName = fileName;
		this.srcFilePath = srcFilePath;
		this.status = status;
		this.remark = remark;
	}

	public ConverterInfo(String entityKey, String fileName, String srcFilePath, Integer converterType, Integer status, String remark) {
		this.entityKey = entityKey;
		this.fileName = fileName;
		this.srcFilePath = srcFilePath;
		this.converterType = converterType;
		this.status = status;
		this.remark = remark;
	}

	public ConverterInfo(String entityKey, String fileName, String srcFilePath, String destFilePath, String captureImgPath,
			Integer converterType, Integer status, String remark) {
		this.entityKey = entityKey;
		this.fileName = fileName;
		this.srcFilePath = srcFilePath;
		this.destFilePath = destFilePath;
		this.captureImgPath = captureImgPath;
		this.converterType = converterType;
		this.status = status;
		this.remark = remark;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the converterType
	 */
	public Integer getConverterType() {
		return converterType;
	}

	/**
	 * @param playMode the converterType to set
	 */
	public void setConverterType(Integer converterType) {
		this.converterType = converterType;
	}

	/**
	 * @return the srcFilePath
	 */
	public String getSrcFilePath() {
		return srcFilePath;
	}

	/**
	 * @param srcFilePath the srcFilePath to set
	 */
	public void setSrcFilePath(String srcFilePath) {
		this.srcFilePath = srcFilePath;
	}

	/**
	 * @return the destFilePath
	 */
	public String getDestFilePath() {
		return destFilePath;
	}

	/**
	 * @param destFilePath the destFilePath to set
	 */
	public void setDestFilePath(String destFilePath) {
		this.destFilePath = destFilePath;
	}

	/**
	 * @return the captureImgPath
	 */
	public String getCaptureImgPath() {
		return captureImgPath;
	}

	/**
	 * @param captureImgPath the captureImgPath to set
	 */
	public void setCaptureImgPath(String captureImgPath) {
		this.captureImgPath = captureImgPath;
	}

	/**
	 * @return the entityKey
	 */
	public String getEntityKey() {
		return entityKey;
	}

	/**
	 * @param entityKey the entityKey to set
	 */
	public void setEntityKey(String entityKey) {
		this.entityKey = entityKey;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the converterTime
	 */
	public Date getConverterTime() {
		return converterTime;
	}

	/**
	 * @param encoderTime the converterTime to set
	 */
	public void setConverterTime(Date converterTime) {
		this.converterTime = converterTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((captureImgPath == null) ? 0 : captureImgPath.hashCode());
		result = prime * result + ((destFilePath == null) ? 0 : destFilePath.hashCode());
		result = prime * result + ((entityKey == null) ? 0 : entityKey.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((converterType == null) ? 0 : converterType.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((srcFilePath == null) ? 0 : srcFilePath.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConverterInfo other = (ConverterInfo) obj;
		if (captureImgPath == null) {
			if (other.captureImgPath != null)
				return false;
		} else if (!captureImgPath.equals(other.captureImgPath))
			return false;
		if (destFilePath == null) {
			if (other.destFilePath != null)
				return false;
		} else if (!destFilePath.equals(other.destFilePath))
			return false;
		if (entityKey == null) {
			if (other.entityKey != null)
				return false;
		} else if (!entityKey.equals(other.entityKey))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (converterType == null) {
			if (other.converterType != null)
				return false;
		} else if (!converterType.equals(other.converterType))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (srcFilePath == null) {
			if (other.srcFilePath != null)
				return false;
		} else if (!srcFilePath.equals(other.srcFilePath))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConverterInfo [id=" + getId() + ",key=" + getKey() + ", version=" + getVersion() + ",fileName=" + fileName + ", playMode="
				+ converterType + ", srcFilePath=" + srcFilePath + ", destFilePath=" + destFilePath + ", captureImgPath=" + captureImgPath
				+ ", entityKey=" + entityKey + ", status=" + status + ", remark=" + remark + "]";
	}

}
