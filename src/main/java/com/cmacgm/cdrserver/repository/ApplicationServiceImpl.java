package com.cmacgm.cdrserver.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmacgm.cdrserver.model.Application;
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService{

	@Autowired(required=true)
	ApplicationRepository applicationRepository;
	@Override
	public Application findById(Long id) {
		// TODO Auto-generated method stub
		 return applicationRepository.findOne(id);
	}
	
	@Override
	public List<Application> findByAppName( String applicationName) {
		// TODO Auto-generated method stub
		 return applicationRepository.findByAppName(applicationName);
	}

}
