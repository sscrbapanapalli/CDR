package com.cmacgm.cdrserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmacgm.cdrserver.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	public User findById(@Param("id") Long id);
	
	public User findByUserId(@Param("userId") String userId);

}
