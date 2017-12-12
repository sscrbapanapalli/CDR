package com.cmacgm.cdrserver.model;

import java.util.Collection;

public class UserHomeModel {
	
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
	 
	public UserHomeModel(){
		
	}
	public UserHomeModel(Collection<Application> applications, Collection<Role> roles){
		this.applications=applications;
		this.roles=roles;
	}
	@Override
	public String toString(){
		final StringBuilder builder=new StringBuilder();
		builder.append("UserHomeModel [applications=").append(applications).append("]").append("[roles=").append(roles).append("]");
		return builder.toString();
	}
	 

}
