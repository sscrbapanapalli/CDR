package com.cmacgm.cdrserver.controller;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmacgm.cdrserver.ActiveDirectory;
import com.cmacgm.cdrserver.model.Application;
import com.cmacgm.cdrserver.model.FrameworkUtil;
import com.cmacgm.cdrserver.model.RetValue;
import com.cmacgm.cdrserver.model.User;
import com.cmacgm.cdrserver.model.UserHomeModel;
import com.cmacgm.cdrserver.model.UserLoginModel;
import com.cmacgm.cdrserver.model.UserModel;
import com.cmacgm.cdrserver.repository.UserRepository;
import com.security.SecurityInfo;
import com.security.SecurityUtil;

/**
 * @filename LoginController.java(To Validate user login credentials across
 *           LDAP)
 * @author Ramesh Kumar B
 * 
 */

@RestController
@RequestMapping("/login")
public class LoginController {

	public static final String HASH_SECRET_KEY = "123CDR@123*!";


	@Autowired(required = true)
	private HttpSession httpSession;

	@Autowired
	UserRepository userRepository;

	private final HashMap map = new HashMap();

	private static final Logger logger = Logger.getLogger(LoginController.class);


	@RequestMapping(value = "/loginUser", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public @ResponseBody RetValue<HashMap> loginUser(@RequestBody UserLoginModel user) throws Exception {
		Boolean authStatus = false;
		try {
			if (user.getUserName() != null && user.getPassword() != null
					|| user.getUserName().trim() != "" && user.getPassword().trim() != "") {
				String username = user.getUserName();			
				String[] words = username.split("@");
				if (words[0] != null)
					username = words[0];
				username = username.toUpperCase();
				// System.out.println(" in cdr server login controller"+username
				// + "-" + password );
				authStatus = ActiveDirectory.getActiveDirectoryAuthentication(username, decrypt(user.getPassword()));
          
			     if(authStatus){
						String token = generateToken(username);
						map.put("userToken", token);
						map.put("authStatus", authStatus);
						httpSession.setAttribute("userName", username);
						httpSession.setAttribute("userToken", token);

						return FrameworkUtil.getResponseValue(true, "success", map);
			     }

			}

			return FrameworkUtil.getResponseValue(true, "failure", null);
		} catch (Exception e) {
			logger.error("This is complete stacktrace", e);
			return FrameworkUtil.getResponseValue(true, "failure", null);
		}
	}
	
	
	 /**
     * Parameter input will be in format of string that will be prepared by
     * concatenating parameters - cipherText, iv, salt, passPhrase, keySize,
     * iterationCount in a random order. And at last indices will also be part
     * of this string where 3 random indices will be converted to corresponding
     * alphabet. e.g. 0-represents a , 1-represents b and so on..
     *
     * At 0th position there will always be CipherText At 1st position -> IV At
     * 2nd position -> SALT At 3rd position -> PassPhrase At 4th position ->
     * IterationCount At 5th position -> KeySize At 6th position -> Indices [ in
     * which above six are randomized]
     *
     * @param input ciphered
     * @return String
     */
    public static String decrypt( String input ) {

        String DL = "__bcdef567kop48__";

        String[] values = input.split( DL );
        // last element indicates indices
        String indices = values[values.length - 1];
        int[] indexes = convert( indices );
        SecurityInfo securityInfo = new SecurityInfo( values, indexes );
        SecurityUtil aesUtil = new SecurityUtil( securityInfo.getKeySize(), securityInfo.getIterationCount() );

        return aesUtil.decrypt( securityInfo.getSalt(), securityInfo.getIv(), securityInfo.getPassPhrase(), securityInfo.getCipherText() );

    }

    public static boolean hasValue( String value ) {
        return !StringUtils.isEmpty( value );
    }

    public static int getInt( String s ) {

        if ( StringUtils.isNumeric(s) && hasValue( s ) ) {
            return Integer.parseInt( s );
        }

        return 0;
    }

    private static int[] convert( String indices ) {
        String[] indexes = indices.split( "," );

        int[] ints = new int[indexes.length];

        for ( int i = 0; i < indexes.length; i++ ) {
            String s = getNum( indexes[i] );
            ints[i] = getInt( s );
        }
        return ints;
    }

    private static String getNum( String s ) {
        switch ( s ) {
            case "a":
                return "0";
            case "b":
                return "1";
            case "c":
                return "2";
            case "d":
                return "3";
            case "e":
                return "4";
            case "f":
                return "5";
        }
        return s;
    }

	public String generateToken(String username) throws Exception {
		String combination = String.valueOf(username) + new Date() + HASH_SECRET_KEY;
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(combination.getBytes());
		byte byteData[] = md.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		String hash = sb.toString();
		return hash;
	}

	@RequestMapping(value = "/logOut/{userToken}", method = RequestMethod.POST)
	@ResponseBody
	public RetValue<Boolean> logOut(@PathVariable("userToken") String userToken) throws Exception {

		if (userToken != null && userToken != "") {
			httpSession.setAttribute("userName", null);
			httpSession.setAttribute("userToken", null);
			map.remove(userToken);
			return FrameworkUtil.getResponseValue(true, HttpStatus.OK.toString(), true);
		}
		return FrameworkUtil.getResponseValue(true, HttpStatus.NOT_FOUND.toString(), false);

	}

	@RequestMapping(value = "/getUserDetails/{userToken}", method = RequestMethod.GET)
	public @ResponseBody RetValue<UserModel> getUserDetails(@PathVariable("userToken") String userToken)
			throws Exception {
		UserModel userModel = null;
		if (httpSession.getAttribute("userName") != null && httpSession.getAttribute("userToken") != null) {
			String userName = (String) httpSession.getAttribute("userName");
			User user = userRepository.findByUserId(userName + "@CMA-CGM.COM");
			if (user != null) {

				userModel = new UserModel();
				userModel.setUserToken(httpSession.getAttribute("userToken").toString());
				userModel.setUserId(userName + "@CMA-CGM.COM");
				userModel.setUserName(userName);
				userModel.setEmail(userName + "@CMA-CGM.COM");
				userModel.setRoleType(user.getRoles().iterator().next().getRoleName());

				return FrameworkUtil.getResponseValue(true, HttpStatus.OK.toString(), userModel);
			}
		}

		return FrameworkUtil.getResponseValue(true, HttpStatus.NOT_FOUND.toString(), null);

	}

	@RequestMapping(value = "/getRole", method = RequestMethod.GET)
	public @ResponseBody RetValue<String> GetRole() throws Exception {
		
		if (httpSession.getAttribute("userName") != null && httpSession.getAttribute("userToken") != null) {
			String userName = (String) httpSession.getAttribute("userName");
			User user = userRepository.findByUserId(userName + "@CMA-CGM.COM");
			if (user != null) {

				return FrameworkUtil.getResponseValue(true, HttpStatus.OK.toString(),
						user.getRoles().iterator().next().getRoleName());
			}
		}

		return FrameworkUtil.getResponseValue(true, HttpStatus.NOT_FOUND.toString(), null);

	}
	
	@RequestMapping(value = "/getUserAuthDetails/{userToken}", method = RequestMethod.GET)
	public UserHomeModel  getUserAuthDetails(@PathVariable("userToken") String userToken)
			throws Exception {
		User user = null;
		UserHomeModel obj=new UserHomeModel();
		Collection<Application> appList=new ArrayList<>();
		if (httpSession.getAttribute("userName") != null && httpSession.getAttribute("userToken") != null
				&& !userToken.isEmpty() && userToken.equals(httpSession.getAttribute("userToken").toString())) {
			String userName = (String) httpSession.getAttribute("userName");
			user = new User();
			user=userRepository.findByUserId(userName + "@CMA-CGM.COM");
			obj.setApplications(user.getApplications());
			obj.setRoles(user.getRoles());
			appList=obj.getApplications();
			
			for(Application obj1:appList){
			System.out.println("application:"+obj1.getappName());}
			
				return obj;
		}
		return  null;

		

	}

}
