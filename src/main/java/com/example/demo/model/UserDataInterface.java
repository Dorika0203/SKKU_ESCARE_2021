package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataInterface extends JpaRepository<UserDataModel, String> {
}