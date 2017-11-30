package com.cmacgm.cdrserver.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.cmacgm.cdrserver.model.Application;

public interface ApplicationService {
	
	public Application findById(@Param("id") Long id);
	public List<Application> findByAppName(@Param("appName") String applicationName);


}
