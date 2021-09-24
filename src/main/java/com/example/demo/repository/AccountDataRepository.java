package com.example.demo.repository;

import java.util.List;

import com.example.demo.model.AccountDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDataRepository extends JpaRepository<AccountDataModel, Long> {

    List<AccountDataModel> findAllByUserId(String userID);

    boolean existsByAccount(String account);

    AccountDataModel findByAccount(String account);
}