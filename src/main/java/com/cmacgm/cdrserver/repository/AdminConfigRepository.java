package com.cmacgm.cdrserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmacgm.cdrserver.model.User;

@Repository
public interface AdminConfigRepository extends JpaRepository<User, Long> {
	
	 

}
