package com.cmacgm.cdrservice.controller;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmacgm.cdrservice.ActiveDirectory;
import com.cmacgm.cdrservice.model.User;


@RestController
@RequestMapping("/login")
public class LoginController {
	
	private static final Logger logger=Logger.getLogger(LoginController.class);
	@CrossOrigin(origins = "*")
	//@CrossOrigin(origins = "http://10.13.44.80:8080/cdrclient")
	@RequestMapping(value = "/loginUser", method = RequestMethod.POST) 
	@ResponseBody public  Boolean  loginUser(@RequestBody  User user) throws Exception   {
	
		Boolean authStatus=false;
		String token=null;
		//HashMap map=new HashMap();
		try{
			if (user.getUsername()!=null && user.getPassword() !=null ||user.getUsername().trim()!="" && user.getPassword().trim() !="") {
				String username=user.getUsername();
				String password=user.getPassword();
		        String[] words = username.split("@");
				if(words[0]!=null)
				 username=words[0];
						
				username=username.toUpperCase();
				System.out.println(" in cdr server login controller"+username + "-" + password );
				authStatus = ActiveDirectory.getActiveDirectoryAuthentication(username, password);
				System.out.println(authStatus);
				return authStatus;
				/*System.out.println(authStatus);
					  if(!authStatus){	
						 // map.put("message", "Invalid Credentials");				
							logger.info(username+":Invalid Credentials");
						return "false";
						}
					  else return "true" ;*/
												
	          }
			
			return authStatus;
			  }catch(Exception e){
				  logger.error("This is complete stacktrace", e);
				  return authStatus;
			  }	
	}
	
	
}
