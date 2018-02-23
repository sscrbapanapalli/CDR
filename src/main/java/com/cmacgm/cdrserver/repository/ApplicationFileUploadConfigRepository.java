package com.cmacgm.cdrserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmacgm.cdrserver.model.Application;
import com.cmacgm.cdrserver.model.ApplicationFileUploadConfig;

@Repository
public interface ApplicationFileUploadConfigRepository extends JpaRepository<ApplicationFileUploadConfig, Long>{
	
	public ApplicationFileUploadConfig findById(@Param("id") Long id);
	
	public List<ApplicationFileUploadConfig> findByApplication(@Param("application") Application application);
	

}
