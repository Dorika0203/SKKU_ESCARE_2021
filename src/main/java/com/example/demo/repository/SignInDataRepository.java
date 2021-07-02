package com.example.demo.repository;

import com.example.demo.model.SignInDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignInDataRepository extends JpaRepository<SignInDataModel, String> {
    List<SignInDataModel> findAllByUserId (String id);
}