package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDataInterface extends JpaRepository<AccountDataModel, String> {
}