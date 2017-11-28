package com.cmacgm.cdrservice.controller;

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

import com.cmacgm.cdrservice.model.Application;
import com.cmacgm.cdrservice.repository.CdrRepository;

@RestController
@RequestMapping("/api")
public class CdrController {
	
	private static final Logger logger = LoggerFactory.getLogger(CdrController.class);
	
	@Autowired
	CdrRepository cdrRepository;
	
	@GetMapping("/serverfolders")
	public List<Application> getServerFolders(){
		return cdrRepository.findAll();
	}

	// Get a Single Note
	@GetMapping("/serverfolder/{id}")
	public ResponseEntity<Application> getFolderById(@PathVariable(value = "id") Long noteId) {
		Application note = cdrRepository.findOne(noteId);
		    if(note == null) {
		        return ResponseEntity.notFound().build();
		    }
		    return ResponseEntity.ok().body(note);
		
	}
	@CrossOrigin(origins = "*")
	//@CrossOrigin(origins = "http://10.13.44.80:8080/cdrclient")
	@GetMapping("/serverfolders/{applicationName}")
	public List<Application> getfolderByApp(@PathVariable(value = "applicationName") String applicationName) {
	   List<Application> note = cdrRepository.findfolderByApp(applicationName);
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
	    //String applicationName="getpaid";	 	
	    String applicationName=request.getParameter("Application");	 
	    System.out.println("appName in server" + applicationName);
	    List<Application> folderList=cdrRepository.findfolderByApp(applicationName);
		MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
	     
	    	
	        Iterator<String> fileNames = multipart.getFileNames();
	         while (fileNames.hasNext()) { // Get List of files from Multipart Request.
	       
	            MultipartFile fileContent = multipart.getFile(fileNames.next());
	            String folderpath=folderList.get(i).getFolderPath();
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
			stream.close();
			return serverResponse;
			
		}
		
	}
}
