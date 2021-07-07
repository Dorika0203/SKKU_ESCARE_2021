package com.example.demo.repository;

import com.example.demo.model.AccountDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDataRepository extends JpaRepository<AccountDataModel, Long> {

}