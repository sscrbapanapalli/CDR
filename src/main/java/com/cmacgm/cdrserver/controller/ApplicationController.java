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
	    String etlStatus="null";
	    String s1="null";
	    String etlProcessed="N";
	    List<BatchFileDetail> batchFileDetailList=new ArrayList<>();
	    BatchHistoryDetail batchHistoryDetail=new BatchHistoryDetail();
	    BatchHistoryDetail batchuploadCheck=new BatchHistoryDetail();
	    BatchHistoryDetail obj=new BatchHistoryDetail();
	    List<String> fileList=new ArrayList<>();
	   // boolean activeIndicator=true;
	    
	    System.out.println("server side appID:" + request.getParameter("applicationId"));
	    
	    applicationId=Long.parseLong(request.getParameter("applicationId"));
	    userName=request.getParameter("userName");
	    batchUploadMonth=request.getParameter("selectedMonth");
	    System.out.println("Selected month" + batchUploadMonth); 
	    //System.out.println("appName in server" + applicationId);
	    
	    Application application = applicationRepository.findById(applicationId);
	    List<ApplicationFileUploadConfig> applicationFileUploadConfig = applicationFileUploadConfigRepository.findByApplication(application);
	    Date dNow = new Date();
	       SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
	       String batchId = applicationId+ft.format(dNow);
	       
	    /*Calendar c = Calendar.getInstance();
		   String batchUploadMonth=c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH );*/
		   
		
		MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
		//batchuploadCheck=batchHistoryDetailsRepository.findByAppIdAndBatchUploadMonthAndBatchUploadStatus(applicationId,batchUploadMonth,batchUploadStatus);
		//System.out.println("batchuploadCheck:" + batchuploadCheck);
		System.out.println("null check" + batchUploadMonth +  ":"+ +applicationId);
		obj=batchHistoryDetailsRepository.findByTop(batchUploadMonth,applicationId);
		System.out.println("test" + obj);
		
		/*if(obj==null || obj.getEtlProcessed()==null || obj.getEtlProcessed().equals("X") ){ //Validation check- upload already completed for selected month or not
*/		if(obj==null || obj.getEtlProcessed()==null || obj.getBatchUploadStatus().equals("Reverse") ){ //Validation check- upload already completed for selected month or not
			 
			//System.out.println("batchuploadCheck is null1");
	        Iterator<String> fileNames = multipart.getFileNames();
	        while(fileNames.hasNext()){
	        	MultipartFile fileContents = multipart.getFile(fileNames.next());
	        	fileList.add(fileContents.getOriginalFilename());
	        }
	        
	        Set<String> filteredSet = new HashSet<String>(fileList);
	        
	        /*System.out.println("file list size" + fileList.size());
	        System.out.println("file list size" + filteredSet.size());
	        System.out.println("actu size"+ applicationFileUploadConfig.size());*/
	        if(batchUploadMonth!=null && batchUploadMonth.length()!=0)	{
	        	System.out.println("test month:" +  batchUploadMonth);
	        if(fileList.size()==applicationFileUploadConfig.size()){
	        if(fileList.size()==filteredSet.size()){
	        
	        	System.out.println("in side second while loop");
	        	Iterator<String> uploadFileNames = multipart.getFileNames();
	        	while (uploadFileNames.hasNext()) { // Get List of files from Multipart Request.
	        	 
	            MultipartFile fileContent = multipart.getFile(uploadFileNames.next());
	            String folderCaption=applicationFileUploadConfig.get(i).getFolderCaption();
	            //String folderpath=applicationFileUploadConfig.get(i).getFileTrgtPath()+folderCaption;
	            String folderpath=applicationFileUploadConfig.get(i).getFileTrgtPath();
	            String fileName=applicationFileUploadConfig.get(i).getFileNamePrefix();
	            String orgFileName=fileContent.getOriginalFilename();
	            System.out.println("check file anme format" +fileName);
	            File dir = new File(folderpath);
	            if (!dir.exists())
					dir.mkdirs();
	            
	            String fileTrgtPath= dir.getAbsolutePath() + File.separator + fileName;
				System.out.println("file target path"+ fileTrgtPath );		
	            BatchFileDetail batchFileDetail=new BatchFileDetail();
	            //batchFileDetail.setActiveIndicator(activeIndicator);
	            batchFileDetail.setBatchFileName(orgFileName);
	            batchFileDetail.setBatchFileTrgtPath(fileTrgtPath);
	            batchFileDetail.setFolderCaption(folderCaption);
	            batchFileDetail.setCreatedBy(userName);
	            batchFileDetail.setUpdatedBy(userName);
	            batchFileDetailList.add(batchFileDetail);
	            	            
	            System.out.println("Check Folder Path in server"+folderpath);
	            
	            uploadResponse=uploadMultipleFileHandler(fileContent,folderpath,fileName);
	             
	           i++;
	            
	            //uploadMultipleFileHandler(fileContent,i);
	            System.out.println("UploadscenarioFiles ion loop uploadResponse:"+uploadResponse);
	        }
	        }
	        	else{
	        		uploadResponse="Please remove duplicate files ";
	        	}
	        }
	        else{
	        	uploadResponse="Please select all files ";
	        }
		}else{
			uploadResponse="Please select upload month";
		}
		}else{
			System.out.println("batchuploadCheck is null sdfsdgs"+ uploadResponse);
			uploadResponse="Batch Upload failed as selected month batch already uploaded with batch id: "+ obj.getBatchId();
			
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
	        	 batchHistoryDetail.setEtlProcessed(etlProcessed);
	        	 batchHistoryDetailsRepository.save(batchHistoryDetail);
             }
	         System.out.println("UploadscenarioFiles uploadResponse:"+uploadResponse);
			return uploadResponse;
	    }
	    
 
	    private String uploadMultipleFileHandler( MultipartFile file,String folderPath,String fileName) throws IOException {
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
						+ File.separator + fileName);
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
				System.out.println(e);
				e.getMessage();
				 serverResponse="Files Upload Failure,Please contact Application Support Team";
					return serverResponse; 
			}
		finally{
			
			return serverResponse;
			
		}
		
	}
	    
	    @GetMapping("/currentUploadDetails/{appId}")
		public BatchHistoryDetail getCurrentUpload(@PathVariable(value="appId") Long appId){
			System.out.println("in /api/batchHistoryDetails");
			String batchUploadStatus="Upload";
			String batchReverseStatus="Reverse";
			String etlProcessedNew="N";
	    	String etlProcessedCompleted="Y";
	    	List<BatchHistoryDetail> reverseBatchdetailsList;
	    	reverseBatchdetailsList=batchHistoryDetailsRepository.getReverseBatchDetails(appId,batchReverseStatus);
	    	for(BatchHistoryDetail list:reverseBatchdetailsList){
	    		System.out.println(" reverse ID" +list.getId());
	    	}
	    	System.out.println("size" + reverseBatchdetailsList.size());
	    	/*if(test=="true"){
	    	BatchHistoryDetail test1=batchHistoryDetailsRepository.findByEtlProcessed(batchUploadStatus,appId,etlProcessedNew,etlProcessedCompleted);
	    	return test1;
	    	}else{
	    		return null;
	    		
	    	}*/
	    	if(reverseBatchdetailsList.size()!=0){
	    		
			 	return batchHistoryDetailsRepository.findByEtlProcessed(batchUploadStatus,appId,etlProcessedNew,etlProcessedCompleted);	
	    	}else
	    		return null;
			
		}
	    
	    
	  /*  @RequestMapping(value = "/getCurrentUpload", method = RequestMethod.POST , produces="application/json")
	    public BatchHistoryDetail getCurrentUpload(String batchUploadStatus,Long appId,String etlProcessedNew,String etlProcessedCompleted){
	    	BatchHistoryDetail batchUpdate= new BatchHistoryDetail();
	    	
	    	batchUpdate=batchHistoryDetailsRepository.findByEtlProcessed(batchUploadStatus,appId,etlProcessedNew,etlProcessedCompleted);
	    	return batchUpdate;
	    }*/
	    
	    
	    @RequestMapping(value = "/reverseUpload", method = RequestMethod.POST , produces="application/json")
	    public @ResponseBody String reverseUpload(HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
	    	System.out.println("In Do Reverse Method"); 
	    	String serverResponse="";
	    	String batchUploadStatus="Upload";
	    	String batchUploadStatusModified="Reverse";
	    	String etlStatusBeforereverse="";
	    	String etlProcessedNew="N";
	    	String etlProcessedCompleted="Y";
	    	String etlModified="X";
	    	String source="";
	    	String destination="";
	    	String userName=request.getParameter("userName");
	    	Long appId=Long.parseLong(request.getParameter("applicationId"));
	    	Long selectedBatchUniqueId=Long.parseLong(request.getParameter("selectedBatchUniqueId"));
	    	/* Date dNow = new Date();
		       SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
		       String batchId = appId+ft.format(dNow);
	    	System.out.println("new format batch id" + batchId);*/
	    	BatchHistoryDetail batchUpdate= new BatchHistoryDetail();
	    	
	    	batchUpdate=batchHistoryDetailsRepository.findById(selectedBatchUniqueId);
	    	System.out.println("test" + batchUpdate);
	    	if(batchUpdate!=null){
	    		etlStatusBeforereverse=batchUpdate.getEtlProcessed();	
	    	batchUpdate.setEtlProcessed(etlModified);		
	    	batchUpdate.setUpdatedBy(userName);
	    	batchHistoryDetailsRepository.save(batchUpdate);
	    	System.out.println("Reverse batch updated entry" + batchUpdate);
	    	
	    	//To move files from destination to archive path
	    	 Application application = applicationRepository.findById(appId);
	 	    List<ApplicationFileUploadConfig> applicationFileUploadConfig = applicationFileUploadConfigRepository.findByApplication(application);
	    	
	 	   source=applicationFileUploadConfig.get(1).getFileTrgtPath();
	 	   destination=applicationFileUploadConfig.get(1).getFileAckPath();
	 	   File destDir = new File(destination);
           File srcDir = new File(source);
           /* for(ApplicationFileUploadConfig archiveProcess:applicationFileUploadConfig){
  		 source=archiveProcess.getFileTrgtPath();
  		 destination=archiveProcess.getFileAckPath()+batchId;
  		File destDir = new File(destination);
          File srcDir = new File(source);
          String ArchiveResult=archiveFiles(destDir,srcDir);
  	}*/
  	//String ArchiveResult=archiveFiles(destDir,srcDir);
	    	
	    	
	    	// To insert new record for Reverse process
	    	BatchHistoryDetail batchReverse= new BatchHistoryDetail();
	    	batchReverse.setAppId(batchUpdate.getAppId()); 
	    	//batchReverse.setBatchFileDetailList(batchUpdate.getBatchFileDetailList());
	    	batchReverse.setBatchId(batchUpdate.getBatchId());
	    	batchReverse.setBatchUploadMonth(batchUpdate.getBatchUploadMonth());
       	// batchHistoryDetail.setActiveIndicator(activeIndicator);
	    	batchReverse.setBatchUploadStatus(batchUploadStatusModified);
	    	batchReverse.setBatchUploadUserName(batchUpdate.getBatchUploadUserName());
	    	batchReverse.setCreatedBy(userName);
	    	batchReverse.setUpdatedBy(userName);
	    	
	    	if(etlStatusBeforereverse.equals(etlProcessedNew)){
			System.out.println("in update record before "+ etlStatusBeforereverse);
			batchReverse.setEtlProcessed(etlModified);
			System.out.println("in update record after"+ batchUpdate.getEtlProcessed());
		}else if(etlStatusBeforereverse.equals(etlProcessedCompleted)){
			batchReverse.setEtlProcessed(etlProcessedNew);
		}	 
		/*batchReverse.setEtlProcessed(batchUpdate.getEtlProcessed());*/
	    	System.out.println("Reverse batch new enrty" + batchReverse);
	    	batchHistoryDetailsRepository.save(batchReverse);
	    	
	    	serverResponse="Reverse for batch id: "+batchUpdate.getBatchId()+" for uploaded month "+batchUpdate.getBatchUploadMonth()+" completed";
	    	
	    	System.out.println(" reverseResponse:"+serverResponse);
	    	}else{
	    		serverResponse="No batch found to reverse"; 
	    		System.out.println("reverseResponse:"+serverResponse);
	    	}
	    	logger.info(serverResponse);
	    	
	    	return serverResponse;
	    }
	    
	   
	    
	    public String archiveFiles(File srcDir,File arcDir){
	    	String archiveResult="";
	    	 try {
	             // Move the source directory to the destination directory.
	             // The destination directory must not exists prior to the
	             // move process.
	             FileUtils.moveDirectory(srcDir, arcDir);
	              archiveResult= "Files moved to Archive Folder";
	         } catch (IOException e) {
	             e.printStackTrace();
	              archiveResult="Failed to move files to archive folder";
	         }
	    	 logger.info(archiveResult);
	    	return archiveResult;
	    }
	    
}


