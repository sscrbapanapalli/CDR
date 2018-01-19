package com.cmacgm.cdrserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmacgm.cdrserver.model.Application;
import com.cmacgm.cdrserver.model.Role;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long>{



}
