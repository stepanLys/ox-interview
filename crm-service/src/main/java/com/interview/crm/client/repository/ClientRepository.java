package com.interview.crm.client.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.interview.crm.client.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    @EntityGraph(attributePaths = {"contacts"})
    List<Client> findAll(Specification<Client> spec);
    
    @EntityGraph(attributePaths = {"contacts"})
    Optional<Client> findById(Long id);
}
