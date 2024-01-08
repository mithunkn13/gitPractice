package com.blog.repository;


import com.blog.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Role,Long>
{
    Optional<Role> findByName(String name);
}
