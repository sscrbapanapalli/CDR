package com.cmacgm.cdrserver.model;

import java.util.List;

public class ApplicationConfig {
	
	private String applicationName;
	private String targetPath;
	private String archivePath;
	private String userName;
	private List<String> selectedFileType;
	private List<FolderMapping> folderMapping;
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getTargetPath() {
		return targetPath;
	}
	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}
	public String getArchivePath() {
		return archivePath;
	}
	public void setArchivePath(String archivePath) {
		this.archivePath = archivePath;
	}
	public List<String> getSelectedFileType() {
		return selectedFileType;
	}
	public void setSelectedFileType(List<String> selectedFileType) {
		this.selectedFileType = selectedFileType;
	}
	public List<FolderMapping> getFolderMapping() {
		return folderMapping;
	}
	public void setFolderMapping(List<FolderMapping> folderMapping) {
		this.folderMapping = folderMapping;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}


}
