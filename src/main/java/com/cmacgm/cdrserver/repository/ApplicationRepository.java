package com.cmacgm.cdrserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmacgm.cdrserver.model.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>{
	
	public Application findById(@Param("id") Long id);
	
	public Application findByAppName(@Param("appName") String appName);

}
