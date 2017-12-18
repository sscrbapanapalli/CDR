package com.cmacgm.cdrserver.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cmacgm.cdrserver.model.BatchFileDetail;
import com.cmacgm.cdrserver.model.BatchHistoryDetail;
import com.cmacgm.cdrserver.model.User;
import com.cmacgm.cdrserver.repository.ApplicationFileUploadConfigRepository;
import com.cmacgm.cdrserver.repository.ApplicationRepository;
import com.cmacgm.cdrserver.repository.BatchHistoryDetailsRepository;
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
	BatchHistoryDetailsRepository batchHistoryDetailsRepository;
	
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
	
	/* 
	 * To get batch History Details
	 */
	
	@GetMapping("/batchHistoryDetails/{appId}")
	public List<BatchHistoryDetail> getBatchHistoryDetails(@PathVariable(value="appId") Long appId){
		System.out.println("in /api/batchHistoryDetails");
		return batchHistoryDetailsRepository.findByAppId(appId);		
		
	}
	
	
	 @RequestMapping(value = "/uploadfile", method = RequestMethod.POST , produces="application/json")
	    public @ResponseBody String UploadscenarioFiles(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
	    int i=0;
	    String uploadResponse="";
	    String userName="";
	    String batchUploadMonth="";
	    String batchUploadStatus="Upload";
	    Long applicationId=0l;
	    List<String> fileList=new ArrayList<>();
	    boolean dupFile=true;
	   // boolean activeIndicator=true;
	    applicationId=Long.parseLong(request.getParameter("applicationId"));
	    userName=request.getParameter("userName");
	    batchUploadMonth=request.getParameter("selectedMonth");
	    System.out.println("appName in server" + applicationId);
	    Application application = applicationRepository.findById(applicationId);
	    List<BatchFileDetail> batchFileDetailList=new ArrayList<>();
	    BatchHistoryDetail batchHistoryDetail=new BatchHistoryDetail();
	    BatchHistoryDetail batchuploadCheck=new BatchHistoryDetail();
	    Date dNow = new Date();
	       SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
	       String batchId = ft.format(dNow);
	       
	    /*Calendar c = Calendar.getInstance();
		   String batchUploadMonth=c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH );*/
		   
		List<ApplicationFileUploadConfig> applicationFileUploadConfig = applicationFileUploadConfigRepository.findByApplication(application);
		MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
		batchuploadCheck=batchHistoryDetailsRepository.findByAppIdAndBatchUploadMonthAndBatchUploadStatus(applicationId,batchUploadMonth,batchUploadStatus);
		System.out.println("batchuploadCheck:" + batchuploadCheck);
		if(batchuploadCheck == null){ //Validation check- upload already completed for selected month or not
			System.out.println("batchuploadCheck is null");
	        Iterator<String> fileNames = multipart.getFileNames();
	        while(fileNames.hasNext()){
	        	MultipartFile fileContents = multipart.getFile(fileNames.next());
	        	fileList.add(fileContents.getOriginalFilename());
	        }
	        
	        Set<String> filteredSet = new HashSet<String>(fileList);
	        
	        System.out.println("file list size" + fileList.size());
	        System.out.println("file list size" + filteredSet.size());
	        if(fileList.size()==filteredSet.size()){
	        	System.out.println("in side second while loop");
	        	Iterator<String> uploadFileNames = multipart.getFileNames();
	         while (uploadFileNames.hasNext()) { // Get List of files from Multipart Request.
	        	 
	            MultipartFile fileContent = multipart.getFile(uploadFileNames.next());
	           
	            String folderpath=applicationFileUploadConfig.get(i).getFileTrgtPath();
	            String folderCaption=applicationFileUploadConfig.get(i).getFolderCaption();
	            File dir = new File(folderpath);
	            if (!dir.exists())
					dir.mkdirs();
	            
	            String fileTrgtPath= dir.getAbsolutePath() + File.separator + fileContent.getOriginalFilename();
				System.out.println("file target path"+ fileTrgtPath );		
	            BatchFileDetail batchFileDetail=new BatchFileDetail();
	            //batchFileDetail.setActiveIndicator(activeIndicator);
	            batchFileDetail.setBatchFileName(fileContent.getOriginalFilename());
	            batchFileDetail.setBatchFileTrgtPath(fileTrgtPath);
	            batchFileDetail.setFolderCaption(folderCaption);
	            batchFileDetail.setCreatedBy(userName);
	            batchFileDetail.setUpdatedBy(userName);
	            batchFileDetailList.add(batchFileDetail);
	            	            
	            System.out.println("Check Folder Path in server"+folderpath);
	            
	            uploadResponse=uploadMultipleFileHandler(fileContent,folderpath);
	             
	           i++;
	            
	            //uploadMultipleFileHandler(fileContent,i);
	            System.out.println("UploadscenarioFiles ion loop uploadResponse:"+uploadResponse);
	        }
	        }
	        else{
	        	uploadResponse="Please remove duplicate files";
	        }
		}else{
			System.out.println("batchuploadCheck is null"+ uploadResponse);
			uploadResponse="Batch Upload for selected month: "+batchUploadMonth+" completed with batch id: "+ batchuploadCheck.getBatchId();
			
		}
	         
	         if(uploadResponse=="Files Uploaded Successfully"){
	        	 logger.info("Upload file status to server="
							+ uploadResponse);
	        	 
	        	 batchHistoryDetail.setAppId(applicationId); 
	        	 batchHistoryDetail.setBatchFileDetailList(batchFileDetailList);
	        	 batchHistoryDetail.setBatchId(batchId);
	        	 batchHistoryDetail.setBatchUploadMonth(batchUploadMonth);
	        	// batchHistoryDetail.setActiveIndicator(activeIndicator);
	        	 batchHistoryDetail.setBatchUploadStatus(batchUploadStatus);
	        	 batchHistoryDetail.setBatchUploadUserName(userName);
	        	 batchHistoryDetail.setCreatedBy(userName);
	        	 batchHistoryDetail.setUpdatedBy(userName);
	        	 batchHistoryDetailsRepository.save(batchHistoryDetail);
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
				System.out.println(serverFile);	
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
