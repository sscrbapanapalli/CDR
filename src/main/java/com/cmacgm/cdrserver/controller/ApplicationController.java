package com.cmacgm.cdrserver.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cmacgm.cdrserver.model.Application;
import com.cmacgm.cdrserver.model.ApplicationFileUploadConfig;
import com.cmacgm.cdrserver.model.User;
import com.cmacgm.cdrserver.repository.ApplicationFileUploadConfigRepository;
import com.cmacgm.cdrserver.repository.ApplicationRepository;
import com.cmacgm.cdrserver.repository.UserRepository;

/**
 * @filename CdrController.java(To get application vise folder path and to upload file to respective path)
* @author Ramesh Kumar B

*/

@RestController
@RequestMapping("/api")
public class ApplicationController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
	
	@Autowired
	ApplicationRepository applicationRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ApplicationFileUploadConfigRepository applicationFileUploadConfigRepository;
	
	@GetMapping("/applications")
	public List<Application> getServerFolders(){
		return applicationRepository.findAll();
	}
	
	@GetMapping("/users")
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	@GetMapping("/user/{id}")
	public User getUserById(@PathVariable(value = "id") Long id){
		return userRepository.findById(id);
	}
	
	@GetMapping("/userById/{userId}")
	public User getUSerByUserId(@PathVariable(value = "userId") String userId){
		return userRepository.findByUserId(userId);
	}

	@CrossOrigin(origins = "*")
	//@CrossOrigin(origins = "http://10.13.44.80:8080/cdrclient")
	@GetMapping("/serverfolders/{id}")
	public List<ApplicationFileUploadConfig> getfolderByApp(@PathVariable(value = "id") Long id) {
         Application application = applicationRepository.findById(id);
		
		List<ApplicationFileUploadConfig> note = applicationFileUploadConfigRepository.findByApplication(application);
	   /* if(note == null) {
	        return ResponseEntity.notFound().build();
	    }*/
	   System.out.println("list generate");
	    return note;
	}
	
	@CrossOrigin(origins = "*")
	//@CrossOrigin(origins = "http://localhost:8080/cdrclient")
	 @RequestMapping(value = "/uploadfile", method = RequestMethod.POST , produces="application/json")
	    public @ResponseBody String UploadscenarioFiles(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
	    int i=0;
	    String uploadResponse="";
	    //String applicationName="getpaid";	 	-
	    Long applicationId=0l;
	    applicationId=Long.parseLong(request.getParameter("applicationId"));	 
	    System.out.println("appName in server" + applicationId);
	    Application application = applicationRepository.findById(applicationId);
		
		List<ApplicationFileUploadConfig> applicationFileUploadConfig = applicationFileUploadConfigRepository.findByApplication(application);
		MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
	     
	    	
	        Iterator<String> fileNames = multipart.getFileNames();
	         while (fileNames.hasNext()) { // Get List of files from Multipart Request.
	       
	            MultipartFile fileContent = multipart.getFile(fileNames.next());
	            String folderpath=applicationFileUploadConfig.get(i).getFileTrgtPath();
	            System.out.println("Check Folder Path in server"+folderpath);
	             uploadResponse=uploadMultipleFileHandler(fileContent,folderpath);
	            i++;
	            
	            //uploadMultipleFileHandler(fileContent,i);
	            System.out.println("UploadscenarioFiles ion loop uploadResponse:"+uploadResponse);
	        }
	         System.out.println("UploadscenarioFiles uploadResponse:"+uploadResponse);
			return uploadResponse;
	    }
	    
 
	    private String uploadMultipleFileHandler( MultipartFile file,String folderPath) throws IOException {
	    	String serverResponse="";
	    	BufferedOutputStream stream=null;
			
			try {
				byte[] bytes = file.getBytes();
				//System.out.println("dir number"+i);
				// Creating the directory to store file
				//String rootPath = "\\\\10.13.44.80\\c$\\CDR"+i;
				//String rootPath = "\\\\10.13.44.80\\c$\\CDR";
				File dir = new File(folderPath);
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + file.getOriginalFilename());
				stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				logger.info("Server File Location="
						+ serverFile.getAbsolutePath());
				 serverResponse="Files Uploaded Successfully";
				 System.out.println("UploadscenarioFiles serverResponse:"+serverResponse);
			return serverResponse;
			} catch (Exception e) {
				e.getMessage();
				 serverResponse="Files Upload Failure";
					return serverResponse; 
			}
		finally{
			
			return serverResponse;
			
		}
		
	}
}
