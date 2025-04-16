package com.example.web_adventure.repository;

import com.example.web_adventure.model.CourseDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseDomainRepository extends JpaRepository<CourseDomain, Long> {
    
    List<CourseDomain> findByNameContainingIgnoreCase(String name);
    
    List<CourseDomain> findByParentDomainId(Long parentDomainId);
    
    List<CourseDomain> findByParentDomainIsNull();
}