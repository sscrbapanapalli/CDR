package com.cmacgm.cdrserver.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmacgm.cdrserver.model.Application;
import com.cmacgm.cdrserver.model.Role;
import com.cmacgm.cdrserver.model.User;
import com.cmacgm.cdrserver.repository.AdminConfigRepository;
import com.cmacgm.cdrserver.repository.ApplicationRepository;
import com.cmacgm.cdrserver.repository.RolesRepository;
import com.cmacgm.cdrserver.repository.UserRepository;

@RestController
@RequestMapping("/admin")
public class AdminConfigController {
	
	@Autowired
	UserRepository userRepository;
	
	 @Autowired
	 AdminConfigRepository adminConfigRepository;
	 
	 @Autowired
		ApplicationRepository applicationRepository;
	 
	 @Autowired
		RolesRepository roleRepository;
	 
	 
	
	 @GetMapping("/getAllApplications")
		public List<Application> getAllApplications(){
		 System.out.println(" for all applications");
		 
			return applicationRepository.findAll();
		}
	 
	 @GetMapping("/getAllRoles")
		public List<Role> getAllRoles(){
		 System.out.println(" for all roles");
			return roleRepository.findAll();
		}
	
	

	 @RequestMapping(value = "/setUserConfiguration", method = RequestMethod.POST , produces="application/json")
	    public @ResponseBody String setUserConfiguration(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		
		 String configResponse="Success";
		 String userName=request.getParameter("userName");
		 String roleId=request.getParameter("roleId");
		 String appId=request.getParameter("selectedAppId");
		 String createdBy=request.getParameter("createdBy");
		 String userDisplayName=null;
		 User user=new User();
		 User checkUser=new User();
		 checkUser=userRepository.findByUserId(userName);
		 Application application=null;
		 List<Application> applicationList=new ArrayList<Application>();
		 Role role=null;
		 List<Role> roleList=new ArrayList<Role>();
		 
		 System.out.println("In AdminConfigController");
		 System.out.println("userName" + userName);
		 System.out.println("roleId" + roleId);
		 System.out.println("appId" + appId);
		 System.out.println("createdBy" + createdBy);
		 
		 List<String> myListAppIds = new ArrayList<String>(Arrays.asList(appId.split(",")));
		 List<String> myListRoleIds=new ArrayList<String>(Arrays.asList(roleId.split(",")));
		 
		 for(String myListAppId:myListAppIds){
			 application=new Application(); 
		     application.setId(Long.parseLong(myListAppId));
		 	 applicationList.add(application);
		 }
		 for(String myListRoleId:myListRoleIds){
			 role=new Role();
			 role.setId(Long.parseLong(myListRoleId));
			 roleList.add(role);
		
		 }
		 
		 String[] words = userName.split("@");
			if (words[0] != null)
				 userDisplayName = words[0];
			userDisplayName = userDisplayName.toUpperCase();
		 
		 if(checkUser==null){
		 
		/* role.setId(roleId);
		// applicationList.add(application);
		 roleList.add(role);*/
		 user.setUserDisplayName(userDisplayName);
		 user.setUserId(userName);
		 user.setCreatedBy(createdBy);
		 user.setUpdatedBy(createdBy);
		 user.setApplications(applicationList);
		 user.setRoles(roleList);
		 try{
		 adminConfigRepository.save(user);
		 configResponse="Config details saved successfully";
		 return configResponse;
		 }catch(Exception e){
			 configResponse="Failed to save Config details";
			 return configResponse;
		 }
		 }else{
			 
			/*//application.setId(appId);
			 role.setId(roleId);
			// applicationList.add(application);
			 roleList.add(role);*/
			 checkUser.setUserDisplayName(userDisplayName);
			 checkUser.setUserId(userName);
			 checkUser.setUpdatedBy(createdBy);
			 checkUser.setApplications(applicationList);
			 checkUser.setRoles(roleList);
			 try{
			 adminConfigRepository.save(checkUser);
			 configResponse="Config details Updated successfully";
			 return configResponse;
			 }catch(Exception e){
				 configResponse="Failed to save Config details";
				 return configResponse;
			 }
		 }
	    
	 }
}
