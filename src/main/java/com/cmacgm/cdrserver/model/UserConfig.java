package com.cmacgm.cdrserver.model;

import java.util.List;

public class UserConfig {
	
	private String userId;
	
	private List<Application> selectedApplications;
	private List<Role> selectedRoles;
	private boolean userStatus;
	private String createdBy;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<Application> getSelectedApplications() {
		return selectedApplications;
	}
	public void setSelectedApplications(List<Application> selectedApplications) {
		this.selectedApplications = selectedApplications;
	}
	public List<Role> getSelectedRoles() {
		return selectedRoles;
	}
	public void setSelectedRoles(List<Role> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}
	
	public boolean getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(boolean userStatus) {
		this.userStatus = userStatus;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	

}
