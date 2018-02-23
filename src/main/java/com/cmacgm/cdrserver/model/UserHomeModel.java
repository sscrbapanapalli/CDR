package com.cmacgm.cdrserver.model;

import java.util.Collection;
import java.util.Date;

public class UserHomeModel {
	
	private long id;
	private boolean activeIndicator;
	private String userId;
	private String userName; 
	private String createdBy;
	private Date createdDate;
	private Collection<Application> applications;
	private Collection<Role> roles;
	
	public Collection<Application> getApplications() {
		return applications;
	}
	public void setApplications(Collection<Application> applications) {
		this.applications = applications;
	}
	public Collection<Role> getRoles() {
		return roles;
	}
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
	 
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public boolean isActiveIndicator() {
		return activeIndicator;
	}
	public void setActiveIndicator(boolean activeIndicator) {
		this.activeIndicator = activeIndicator;
	}
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public UserHomeModel(){
		
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public UserHomeModel(Collection<Application> applications, Collection<Role> roles){
		this.applications=applications;
		this.roles=roles;
	}
	@Override
	public String toString(){
		final StringBuilder builder=new StringBuilder();
		builder.append("UserHomeModel [applications=").append(applications).append("]").append("[roles=").append(roles).append("]")
		.append("[activeIndicator=").append(activeIndicator).append("]").append("[id=").append(id).append("]")
		.append("[userName=").append(userName).append("]").append("[userId=").append(userId).append("]")
		.append("[createdDate=").append(createdDate).append("]").append("[createdBy=").append(createdBy).append("]");
		return builder.toString();
	}
	 

}
