package com.example.demo.repository;

import com.example.demo.model.AdminDataModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDataRepository extends JpaRepository<AdminDataModel, String> {
    
}