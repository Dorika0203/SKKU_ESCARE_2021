package com.example.demo.repository;

import com.example.demo.model.AccountDataModel;
import com.example.demo.model.SignInDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountDataRepository extends JpaRepository<AccountDataModel, String> {
    List<AccountDataModel> findAllByUserId (String id);
}