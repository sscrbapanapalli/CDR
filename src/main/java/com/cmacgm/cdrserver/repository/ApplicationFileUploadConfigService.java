package com.cmacgm.cdrserver.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.cmacgm.cdrserver.model.Application;
import com.cmacgm.cdrserver.model.ApplicationFileUploadConfig;

public interface ApplicationFileUploadConfigService {
	
	public ApplicationFileUploadConfig findById(@Param("id") Long id);
	public List<ApplicationFileUploadConfig> findByApplication(@Param("application") Application application);


}
