package com.cmacgm.cdrservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmacgm.cdrservice.model.Application;

@Repository
public interface CdrRepository extends JpaRepository<Application, Long>{
	
	@Query( value = "SELECT t FROM Application t where t.applicationName = :id")
	public List<Application> findfolderByApp(@Param("id") String id);

}
