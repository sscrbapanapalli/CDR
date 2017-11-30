package com.cmacgm.cdrserver.repository;

import org.springframework.data.repository.query.Param;

import com.cmacgm.cdrserver.model.User;

public interface UserService {
	
public User findById(@Param("id") Long id);
	
	public User findByUserId(@Param("userId") String userId);


}
