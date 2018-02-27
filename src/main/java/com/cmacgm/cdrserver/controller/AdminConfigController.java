package com.cmacgm.cdrserver.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmacgm.cdrserver.model.Application;
import com.cmacgm.cdrserver.model.ApplicationConfig;
import com.cmacgm.cdrserver.model.ApplicationFileUploadConfig;
import com.cmacgm.cdrserver.model.FolderMapping;
import com.cmacgm.cdrserver.model.Role;
import com.cmacgm.cdrserver.model.User;
import com.cmacgm.cdrserver.model.UserConfig;
import com.cmacgm.cdrserver.model.UserHomeModel;
import com.cmacgm.cdrserver.repository.AdminConfigRepository;
import com.cmacgm.cdrserver.repository.ApplicationFileUploadConfigRepository;
import com.cmacgm.cdrserver.repository.ApplicationRepository;
import com.cmacgm.cdrserver.repository.RolesRepository;
import com.cmacgm.cdrserver.repository.UserRepository;

/**
 * @filename AdminConfigController.java(To set user level access to application)
* @author Ramesh Kumar B

*/

@RestController
@RequestMapping("/admin")
public class AdminConfigController {
	
	
	@Autowired(required = true)
	private HttpSession httpSession;
	
	@Autowired
	UserRepository userRepository;
	
	 @Autowired
	 AdminConfigRepository adminConfigRepository;
	 
	 @Autowired
		ApplicationRepository applicationRepository;
	 
	 @Autowired
		RolesRepository roleRepository;
	 
	 @Autowired
	 ApplicationFileUploadConfigRepository applicationFileUploadConfigRepository;
	 
	 
	
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
	@GetMapping("/getAllUserDetails")
	public List<UserHomeModel> getAllUserDetails(){
		List<User> userResult=userRepository.findAll();
		List<UserHomeModel> allUserResultList=new ArrayList<>();
		
		for(User testObj:userResult){
			
			Collection<String> appListObj=new ArrayList<>();
			Collection<String> roleListObj=new ArrayList<>();
			
			UserHomeModel obj=new UserHomeModel();
			obj.setId(testObj.getId());
			obj.setUserId(testObj.getUserId());
			obj.setCreatedBy(testObj.getCreatedBy());
			obj.setCreatedDate(testObj.getCreatedDate());
			obj.setUserName(testObj.getUserDisplayName());
			obj.setActiveIndicator(testObj.isActiveIndicator());
			obj.setApplications(testObj.getApplications());
			obj.setRoles(testObj.getRoles());
			
			
			for(Application appObj:testObj.getApplications()){
				appListObj.add(appObj.getappName());
				
			}
			for(Role roleObj:testObj.getRoles()){
				roleListObj.add(roleObj.getRoleName());
				
			}
			obj.setAppList(appListObj);
			obj.setRoleList(roleListObj);
			allUserResultList.add(obj);
			System.out.println("In User all details");
			System.out.println("User testObj"+ ":" + testObj);
			System.out.println("User obj"+ ":" + obj.getRoles());
			System.out.println("User appListObj"+ ":" + obj.getAppList());
			System.out.println("User roleObj"+ ":" + roleListObj);
			
		}
		return allUserResultList;
	}
	
	/*@RequestMapping(value = "/checkUser", method = RequestMethod.POST , produces="application/json")
    public @ResponseBody UserHomeModel checkUser(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		String userStatus="";
		String userName=request.getParameter("userName");
		System.out.println("in check user method" + userName);
		User userObj=userRepository.findByUserId(userName);
		
		UserHomeModel userDetailObj=new UserHomeModel();
		userDetailObj.setId(userObj.getId());
		userDetailObj.setUserId(userObj.getUserId());
		userDetailObj.setCreatedBy(userObj.getCreatedBy());
		userDetailObj.setCreatedDate(userObj.getCreatedDate());
		userDetailObj.setUserName(userObj.getUserDisplayName());
		userDetailObj.setActiveIndicator(userObj.isActiveIndicator());
		userDetailObj.setApplications(userObj.getApplications());
		userDetailObj.setRoles(userObj.getRoles());
		
		System.out.println("in check user method result" + userDetailObj);
		return userDetailObj;
	
	}*/
	
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST , produces="application/json")
    public @ResponseBody String deleteUser(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		String deleteResponse="";
		
		Long id=Long.parseLong(request.getParameter("id"));
		String updatedBy=request.getParameter("updatedBy");
		User user=userRepository.findById(id);
		System.out.println(" in delete reverse method server");
		if(user!=null){
			try{
				user.setActiveIndicator(false);
				System.out.println(user.isActiveIndicator());
				userRepository.save(user);
				deleteResponse="User-" + user.getUserDisplayName() +" Deleted Successfully";
				
			}catch(Exception e){
				deleteResponse="User-" + user.getUserDisplayName() +" failed to Delete ";
			}
			
		}else{
		deleteResponse="User-" + user.getUserDisplayName() +" Not found in DB";
		}
		return deleteResponse;
	}
		

	@RequestMapping(value = "/setUserConfiguration", method = RequestMethod.POST ,consumes="application/json",  produces="application/json")
    public @ResponseBody String setUserConfiguration(@RequestBody UserConfig userConfig) throws IllegalStateException, IOException {
		System.out.println("In User Config method");
		
		 String configResponse="Success";
		 String userId=userConfig.getUserId();
		 userId=userId.toUpperCase();
		 List<Application> selectedAppList=userConfig.getSelectedApplications();
		 List<Role> selectedRoleList=userConfig.getSelectedRoles();
		 boolean userStatus=userConfig.getUserStatus();
		 String createdBy=userConfig.getCreatedBy();
		 
		 String userDisplayName=null;
		 User user=new User();
		 User checkUser=new User();
		 checkUser=userRepository.findByUserId(userId);
		 Application application=null;
		 Role role=null;
		 List<Role> roleList=new ArrayList<Role>();
		 List<Application> applicationList=new ArrayList<Application>();
		 
		 System.out.println("In AdminConfigController");
		 System.out.println("userName" + userId);
		 System.out.println("roleId" + selectedRoleList);
		 System.out.println("appId" + selectedAppList);
		 System.out.println("createdBy" + createdBy);
		 System.out.println("userStatus" + userStatus);
		 
		 /*List<String> myListAppIds = new ArrayList<String>(Arrays.asList(appId.split(",")));
		 List<String> myListRoleIds=new ArrayList<String>(Arrays.asList(roleId.split(",")));*/
		 
		 for(Application myListAppId:selectedAppList){
			 application=new Application(); 
		     application.setId(myListAppId.getId());
		 	 applicationList.add(application);
		 }
		 for(Role myListRoleId:selectedRoleList){
			 role=new Role();
			 role.setId(myListRoleId.getId());
			 roleList.add(role);
		
		 }
		 
		 String[] words = userId.split("@");
			if (words[0] != null)
				 userDisplayName = words[0];
			userDisplayName = userDisplayName.toUpperCase();
		 
		 if(checkUser==null){
		 
		/* role.setId(roleId);
		// applicationList.add(application);
		 roleList.add(role);*/
		 user.setUserDisplayName(userDisplayName);
		 user.setUserId(userId);
		 user.setCreatedBy(createdBy);
		 user.setUpdatedBy(createdBy);
		 user.setApplications(applicationList);
		 user.setRoles(roleList);
		 user.setActiveIndicator(userStatus);
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
			 checkUser.setUserId(userId);
			 checkUser.setUpdatedBy(createdBy);
			 checkUser.setApplications(applicationList);
			 checkUser.setRoles(roleList);
			 checkUser.setActiveIndicator(userStatus);
			 try{
				 System.out.println("In Update User Method");
				 System.out.println(checkUser);
			 adminConfigRepository.save(checkUser);
			 configResponse="Config details Updated successfully";
			 return configResponse;
			 }catch(Exception e){
				 configResponse="Failed to save Config details";
				 return configResponse;
			 }
		 }
	    
	 }
	
	/* Application Configuration */
	
	
	@RequestMapping(value = "/deleteApplication", method = RequestMethod.POST , produces="application/json")
    public @ResponseBody String deleteApplication(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		String deleteResponse="";
		
		Long id=Long.parseLong(request.getParameter("id"));
		String updatedBy=request.getParameter("updatedBy");
		Application application=applicationRepository.findById(id);
		System.out.println(" in deleteApplication method server");
		if(application!=null){
			try{
				application.setActiveIndicator(false);
				application.setUpdatedBy(updatedBy);
				System.out.println(application.isActiveIndicator());
				applicationRepository.save(application);
				deleteResponse="Application-" + application.getappName() +" Deleted Successfully";
				
			}catch(Exception e){
				deleteResponse="Application-" + application.getappName() +" failed to Delete Please contact Application Support Team";
			}
			
		}else{
		deleteResponse="Application-" + application.getappName() +" Not found in DB";
		}
		return deleteResponse;
	}
	
	
	@RequestMapping(value="/updateAppFolderDetails", method=RequestMethod.POST , consumes="application/json", produces="application/json")
	public @ResponseBody String updateAppFolderDetails(@RequestBody List<ApplicationFileUploadConfig> applicationFileUploadConfig) throws Exception{
		System.out.println("In Update Apllication method");
		String updateConfigResponse="";
		String updatedBy = (String) httpSession.getAttribute("userName");
		Application application=new Application();
		List<ApplicationFileUploadConfig> updateList=applicationFileUploadConfig;
		for(ApplicationFileUploadConfig obj:updateList){
			System.out.println("Id:" + obj.getId()+ "" + "folderName" + obj.getFolderCaption()); 
			obj.setUpdatedBy(updatedBy);
			if(obj.getId()!=null){
				application=obj.getApplication();
				System.out.println("appId:" + application.getId());
				System.out.println(application);
				
			try{
				applicationFileUploadConfigRepository.save(obj);
				updateConfigResponse="Application Config Updated Successfully";
			}catch(Exception e){
				updateConfigResponse="Application Config Update Fail,Please contact Application Support Team";
			}
			}else{
				System.out.println("In else method new record");
				/*applicationRepository.save(application);*/
				ApplicationFileUploadConfig newRecord=new ApplicationFileUploadConfig();
				newRecord.setApplication(application);
				newRecord.setCreatedBy(updatedBy);
				newRecord.setUpdatedBy(updatedBy);
				newRecord.setFileAckPath(obj.getFileAckPath());
				newRecord.setFileNamePrefix(obj.getFileNamePrefix());
				newRecord.setFileTrgtPath(obj.getFileTrgtPath());
				newRecord.setFolderCaption(obj.getFolderCaption());
				newRecord.setValidationType(obj.getValidationType());
				try{
					applicationFileUploadConfigRepository.save(newRecord);
					updateConfigResponse="Application Config Updated Successfully";
				}catch(Exception e){
					updateConfigResponse="Application Config Update Fail,Please contact Application Support Team";
				}
			}
		}
		
		
		return updateConfigResponse;
	}
	
	
	
	 @RequestMapping(value = "/setApplicationConfig", method = RequestMethod.POST ,consumes="application/json", produces="application/json")
	    public @ResponseBody String setApplicationConfig(@RequestBody ApplicationConfig applicationConfig) throws IllegalStateException, IOException {
		String configResponse="";
		String fileType="";
		String applicationName=applicationConfig.getApplicationName();
		applicationName=applicationName.toUpperCase();
		String targetPath=applicationConfig.getTargetPath();
		String archivePath=applicationConfig.getArchivePath();
		String createdBy=applicationConfig.getUserName();
		List<String> selectedFileType=applicationConfig.getSelectedFileType();
		List<FolderMapping> folderMapping=applicationConfig.getFolderMapping();
		Application application=new Application();
		Application checkApplication=applicationRepository.findByAppName(applicationName);
		if(checkApplication==null){
		
		application.setappName(applicationName);
		application.setCreatedBy(createdBy);
		application.setUpdatedBy(createdBy);
		applicationRepository.save(application);
		
		fileType=String.join(",", selectedFileType);
		System.out.println(fileType);
	
		for(FolderMapping obj:folderMapping){
			ApplicationFileUploadConfig applicationFileUploadConfig=new ApplicationFileUploadConfig();
			applicationFileUploadConfig.setApplication(application);
			applicationFileUploadConfig.setCreatedBy(createdBy);
			applicationFileUploadConfig.setFileAckPath(archivePath);
			applicationFileUploadConfig.setFileTrgtPath(targetPath);
			applicationFileUploadConfig.setFolderCaption(obj.getFolderName());
			applicationFileUploadConfig.setFileNamePrefix(obj.getFileName());
			applicationFileUploadConfig.setValidationType(fileType);
			applicationFileUploadConfig.setUpdatedBy(createdBy);
			System.out.println(applicationFileUploadConfig);
			try{
			applicationFileUploadConfigRepository.save(applicationFileUploadConfig);
			configResponse="Application Configuration saved successfully";
			
			}catch(Exception e){
				configResponse="Application Configuration failed to save, Please contact Application Support Team";
			}
			
		}
		}else{
			configResponse="Application Configuration already exists";
		}
			
		
		 return configResponse;
	 }
}
