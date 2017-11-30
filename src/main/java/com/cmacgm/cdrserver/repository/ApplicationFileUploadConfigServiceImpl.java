package com.cmacgm.cdrserver.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmacgm.cdrserver.model.Application;
import com.cmacgm.cdrserver.model.ApplicationFileUploadConfig;
@Service
@Transactional
public class ApplicationFileUploadConfigServiceImpl implements ApplicationFileUploadConfigService{

	@Autowired(required=true)
	ApplicationFileUploadConfigRepository applicationFileUploadConfigRepository;
	@Override
	public ApplicationFileUploadConfig findById(Long id) {
		// TODO Auto-generated method stub
		 return applicationFileUploadConfigRepository.findOne(id);
	}
	
	@Override
	public List<ApplicationFileUploadConfig> findByApplication(Application app) {
		// TODO Auto-generated method stub
		 return applicationFileUploadConfigRepository.findByApplication(app);
	}

}
