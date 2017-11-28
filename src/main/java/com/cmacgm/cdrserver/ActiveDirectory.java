
package com.cmacgm.cdrserver; 

import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 *  Active Directory using Java
 * 
 * @filename ActiveDirectory.java
 * @author Ramesh Kumar B
 
 */
public class ActiveDirectory {
	// Logger
	private static final Logger LOG = Logger.getLogger(ActiveDirectory.class.getName());

   

    /**
     * constructor with parameter for initializing a LDAP context
     * 
     * @param username a {@link java.lang.String} object - username to establish a LDAP connection
     * @param password a {@link java.lang.String} object - password to establish a LDAP connection
     */
    public static Boolean getActiveDirectoryAuthentication(String username, String password) {
    	
    	//System.out.println(" in cdr server Active Directory"+username + "-" + password );
    	 
        Properties properties;
        DirContext dirContext;    
        properties = new Properties();
        
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, "LDAP://ad0001.inchessc.com");
        properties.put(Context.SECURITY_PRINCIPAL, username + "@inchessc.com");
        properties.put(Context.SECURITY_CREDENTIALS, password);
        
        //initializing active directory LDAP connection
        try {
			dirContext = new InitialDirContext(properties);		
			return true;
		} catch (NamingException e) {
			LOG.severe(e.getMessage());
			return false;
		}
        
       
    }
    
        
    
   public static void main (String args[]){
	  System.out.println( ActiveDirectory.getActiveDirectoryAuthentication("SSC.RBAPANAPALLI",""));
	  	   
   }
}
