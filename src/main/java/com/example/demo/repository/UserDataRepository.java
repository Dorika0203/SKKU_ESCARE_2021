package com.example.demo.repository;

import com.example.demo.model.UserDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserDataModel, String> {
}