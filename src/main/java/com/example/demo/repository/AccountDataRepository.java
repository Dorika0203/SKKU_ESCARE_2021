package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.AccountDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDataRepository extends JpaRepository<AccountDataModel, Long> {
    AccountDataModel findByUserId (String userId);

    Boolean existsByUserId (String userId);

    List<AccountDataModel> findAllByUserId(String userID);
}