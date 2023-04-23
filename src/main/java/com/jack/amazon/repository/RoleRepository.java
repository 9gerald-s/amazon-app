package com.jack.amazon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jack.amazon.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>{

}
