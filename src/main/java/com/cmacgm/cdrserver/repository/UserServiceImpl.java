package com.cmacgm.cdrserver.repository;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmacgm.cdrserver.model.User;
@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired(required=true)
	UserRepository userRepository;

	@Override
	public User findById(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}

	@Override
	public User findByUserId(String userId) {
		// TODO Auto-generated method stub
		return userRepository.findByUserId(userId);
	}
	
}
