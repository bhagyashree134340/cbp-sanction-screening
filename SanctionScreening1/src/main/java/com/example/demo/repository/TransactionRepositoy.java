package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Transaction;

@Repository
public interface TransactionRepositoy extends JpaRepository<Transaction, String> {
	
	@Query("select t from Transaction t where t.status = :status")
	public List<Transaction> displayStatus(@Param("status") String status);
	
	
	

}
