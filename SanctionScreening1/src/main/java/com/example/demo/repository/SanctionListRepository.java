package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.SanctionList;

public interface SanctionListRepository extends JpaRepository<SanctionList, Integer> {

}
