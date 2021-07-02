package com.example.demo.repository;

import com.example.demo.model.SignOutDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignOutDataRepository extends JpaRepository<SignOutDataModel, String> {
    List<SignOutDataModel> findAllByUserId(String id);
}