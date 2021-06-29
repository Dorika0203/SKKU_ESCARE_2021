package com.example.demo.repository;

import com.example.demo.model.LoginSessionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginSessionRepository extends JpaRepository<LoginSessionModel, String> {

}